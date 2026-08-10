@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.gradle.api.file.RelativePath
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI
import java.security.MessageDigest
import java.util.Properties

val isFullBuild: Boolean =
    try {
        extra["isFullBuild"] == "true"
    } catch (e: Exception) {
        false
    }

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.aboutlibraries.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.config)
    alias(libs.plugins.osdetector)
    alias(libs.plugins.packagedeps)
}

compose.resources {
    generateResClass = always
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xwhen-guards")
        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xmulti-dollar-interpolation")
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    android {
        namespace = "com.maxrave.simpmusic.composeapp"
        compileSdk = 37
        minSdk = 26
        withJava()
        androidResources {
            enable = true
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        dependencies {
            val composeBom = project.dependencies.platform(libs.compose.bom)
            val koinBom = project.dependencies.platform(libs.koin.bom)
            implementation(composeBom)
            implementation(koinBom)
            implementation(libs.commons.io)
        }
        androidMain.dependencies {
            // 👉 FIXED: Sahi tarike se Firebase BOM aur Remote Config add ki gayi hain
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.10.0"))
            implementation("com.google.firebase:firebase-config")

            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(libs.jetbrains.ui.tooling.preview)
            implementation(libs.constraintlayout.compose)

            api(libs.work.runtime.ktx)

            // Runtime
            api(libs.startup.runtime)

            api(projects.media3)
            api(projects.media3Ui)

            if (isFullBuild) {
                implementation(projects.cast)
            } else {
                implementation(projects.castEmpty)
            }
        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.components.resources)
            implementation(libs.jetbrains.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.material.ripple)

            implementation(libs.ui.tooling.preview)

            api(projects.common)
            api(projects.domain)
            implementation(projects.data)

            if (isFullBuild) {
                api(projects.lastfm)
            } else {
                api(projects.lastfmEmpty)
            }

            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)

            api(libs.coil.compose)
            api(libs.coil.network.okhttp)
            api(libs.kmpalette.core)
            api(libs.kmpalette.network)
            implementation(libs.materialkolor)
            implementation(libs.ktor.client.cio)

            implementation(libs.datastore.preferences)

            implementation(libs.compottie)
            implementation(libs.compottie.dot)
            implementation(libs.compottie.network)
            implementation(libs.compottie.resources)

            implementation(libs.androidx.paging.common)
            implementation(libs.paging.compose)

            implementation(libs.aboutlibraries)
            implementation(libs.aboutlibraries.compose.m3)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            api(libs.markdown)

            implementation(libs.haze)
            implementation(libs.haze.material)

            api(libs.cmptoast)
            implementation(libs.file.picker)

            implementation(libs.liquid.glass)
            implementation(libs.liquid.glass.shape)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.sentry.jvm)
            implementation(libs.native.tray)
            implementation(projects.mediaJvmUi)
        }
    }
}

val mpvCacheDir = layout.buildDirectory.dir("mpv-cache")
val mpvVersion = "0.41.0"
val mpvWinBuildTag = "20260610"
val mpvWinBuildSuffix = "20260610-git-304426c"

fun findDirContaining(
    root: java.io.File,
    namePredicate: (String) -> Boolean,
): java.io.File? =
    root
        .walkTopDown()
        .firstOrNull { it.isFile && namePredicate(it.name) }
        ?.parentFile

fun toolAvailable(tool: String): Boolean =
    try {
        ProcessBuilder(tool)
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start()
            .waitFor()
        true
    } catch (e: java.io.IOException) {
        false
    }

fun runChecked(vararg command: String) {
    val exit = ProcessBuilder(*command).inheritIO().start().waitFor()
    check(exit == 0) { "Command failed (exit $exit): ${command.joinToString(" ")}" }
}

fun runCapturing(vararg command: String): String {
    val process = ProcessBuilder(*command).redirectErrorStream(false).start()
    val output = process.inputStream.bufferedReader().readText()
    val exit = process.waitFor()
    check(exit == 0) { "Command failed (exit $exit): ${command.joinToString(" ")}" }
    return output.trim()
}

fun sha256(file: java.io.File): String {
    val digest = MessageDigest.getInstance("SHA-256")
    file.inputStream().use { stream ->
        val buffer = ByteArray(1 shl 16)
        while (true) {
            val read = stream.read(buffer)
            if (read <= 0) break
            digest.update(buffer, 0, read)
        }
    }
    return digest.digest().joinToString("") { "%02x".format(it) }
}

fun rewriteExecutablePathRefs(
    file: java.io.File,
    prefix: String,
): Int {
    val needle = "@executable_path/lib/".toByteArray(Charsets.US_ASCII)
    val prefixBytes = prefix.toByteArray(Charsets.US_ASCII)
    check(prefixBytes.size <= needle.size) {
        "prefix '$prefix' is longer than '@executable_path/lib/' — paths can only be shortened in place"
    }
    val data = file.readBytes()
    var rewritten = 0
    var i = 0
    outer@ while (i <= data.size - needle.size) {
        for (j in needle.indices) {
            if (data[i + j] != needle[j]) {
                i++
                continue@outer
            }
        }
        var end = i + needle.size
        while (end < data.size && data[end] != 0.toByte()) end++
        val name = data.copyOfRange(i + needle.size, end)
        val replacement = prefixBytes + name
        check(replacement.size <= end - i)
        replacement.copyInto(data, i)
        for (k in i + replacement.size until end) data[k] = 0
        rewritten++
        i = end
    }
    if (rewritten > 0) file.writeBytes(data)
    return rewritten
}

fun codesignAdhoc(dir: java.io.File) {
    val machO = dir.walkTopDown().filter { it.isFile && (it.name.endsWith(".dylib") || it.extension.isEmpty()) }.toList()
    check(machO.isNotEmpty()) { "Nothing to sign in ${dir.absolutePath}" }

    check(toolAvailable("codesign")) {
        "codesign is required to re-sign the patched macOS slice — run this task on a Mac. " +
            "CI does not: it downloads the prebuilt archives instead."
    }
    logger.lifecycle("[mpv-multi] Ad-hoc signing ${machO.size} Mach-O files in ${dir.name}")
    machO.forEach { runChecked("codesign", "--force", "--sign", "-", it.absolutePath) }
}

fun extractMacMpvSlice(
    assetArch: String,
    outputDir: java.io.File,
) {
    val cache = mpvCacheDir.get().asFile
    val zip = cache.resolve("mpv-$mpvVersion-$assetArch.zip")
    downloadIfMissing(
        "https://github.com/mpv-player/mpv/releases/download/v$mpvVersion/mpv-v$mpvVersion-$assetArch.zip",
        zip,
        logPrefix = "mpv-multi",
    )

    val stage = cache.resolve("mac-$assetArch-extract")
    stage.deleteRecursively()
    stage.mkdirs()
    project.copy {
        from(zipTree(zip))
        into(stage)
    }
    val innerTar =
        stage.walkTopDown().firstOrNull { it.isFile && it.name.endsWith(".tar.gz") }
            ?: error("No inner tarball inside ${zip.name}")
    project.copy {
        from(tarTree(resources.gzip(innerTar)))
        into(stage)
    }
    val macOsDir =
        stage.walkTopDown().firstOrNull {
            it.isDirectory && it.name == "MacOS" && it.parentFile?.name == "Contents"
        } ?: error("mpv.app/Contents/MacOS not found inside ${zip.name}")
    val binary = macOsDir.resolve("mpv")
    check(binary.isFile) { "mpv binary missing from ${macOsDir.absolutePath}" }

    outputDir.deleteRecursively()
    outputDir.mkdirs()
    project.copy {
        from(macOsDir.resolve("lib"))
        into(outputDir.resolve("lib"))
    }
    val staged = outputDir.resolve("libmpv.dylib")
    binary.copyTo(staged, overwrite = true)
    staged.setWritable(true)

    var rewritten = rewriteExecutablePathRefs(staged, "@loader_path/lib/")
    outputDir.resolve("lib").listFiles()?.filter { it.isFile && it.name.endsWith(".dylib") }?.forEach { dylib ->
        dylib.setWritable(true)
        rewritten += rewriteExecutablePathRefs(dylib, "@loader_path/")
    }
    check(rewritten > 0) {
        "No @executable_path/lib/ entries found in $assetArch — mpv's macOS layout changed, " +
            "rewriteExecutablePathRefs() needs updating before this slice can be published."
    }
    logger.lifecycle("[mpv-multi] $assetArch: rewrote $rewritten install-name entries")
    codesignAdhoc(outputDir)
}

val mpvSetupMacArmCi by tasks.registering {
    group = "mpv-multi"
    description = "Cross-OS: populate mpv-natives/macos-arm64/ with libmpv + its dylib closure."
    val outputDir = rootDir.resolve("mpv-natives/macos-arm64/")
    inputs.property("mpvVersion", mpvVersion)
    outputs.dir(outputDir)
    doLast { extractMacMpvSlice("macos-15-arm", outputDir) }
}

val mpvSetupMacX64Ci by tasks.registering {
    group = "mpv-multi"
    description = "Cross-OS: populate mpv-natives/macos-x64/ with Intel libmpv + its dylib closure."
    val outputDir = rootDir.resolve("mpv-natives/macos-x64/")
    inputs.property("mpvVersion", mpvVersion)
    outputs.dir(outputDir)
    doLast { extractMacMpvSlice("macos-15-intel", outputDir) }
}

fun extractWindowsMpvSlice(
    arch: String,
    outputDir: java.io.File,
) {
    val sevenZip =
        listOf("7zz", "7z").firstOrNull(::toolAvailable)
            ?: error(
                "7-Zip is required to unpack shinchiro's .7z builds and must be 21.07 or newer. " +
                    "macOS: `brew install sevenzip` (provides 7zz). Ubuntu: install the official " +
                    "7-Zip build — distro p7zip is too old for the ARM64 archive.",
            )
    val cache = mpvCacheDir.get().asFile
    val archive = cache.resolve("mpv-dev-$arch-$mpvWinBuildSuffix.7z")
    downloadIfMissing(
        "https://github.com/shinchiro/mpv-winbuild-cmake/releases/download/" +
            "$mpvWinBuildTag/mpv-dev-$arch-$mpvWinBuildSuffix.7z",
        archive,
        logPrefix = "mpv-multi",
    )
    val extractDir = cache.resolve("mpv-dev-$arch-extract")
    extractDir.deleteRecursively()
    extractDir.mkdirs()
    runChecked(sevenZip, "x", "-y", "-o${extractDir.absolutePath}", archive.absolutePath)

    val dllDir =
        findDirContaining(extractDir) { it.startsWith("libmpv") && it.endsWith(".dll") }
            ?: error("No libmpv*.dll inside ${archive.name}")
    outputDir.deleteRecursively()
    outputDir.mkdirs()
    project.copy {
        from(dllDir)
        into(outputDir)
        include("*.dll")
        includeEmptyDirs = false
    }
}

val mpvSetupWindowsX64Ci by tasks.registering {
    group = "mpv-multi"
    description = "Cross-OS: populate mpv-natives/windows-x64/ with libmpv-2.dll."
    val outputDir = rootDir.resolve("mpv-natives/windows-x64/")
    inputs.property("mpvWinBuildSuffix", mpvWinBuildSuffix)
    outputs.dir(outputDir)
    doLast { extractWindowsMpvSlice("x86_64", outputDir) }
}

val mpvSetupWindowsArmCi by tasks.registering {
    group = "mpv-multi"
    description = "Cross-OS: populate mpv-natives/windows-arm64/ with ARM64 libmpv-2.dll."
    val outputDir = rootDir.resolve("mpv-natives/windows-arm64/")
    inputs.property("mpvWinBuildSuffix", mpvWinBuildSuffix)
    outputs.dir(outputDir)
    doLast { extractWindowsMpvSlice("aarch64", outputDir) }
}

val mpvSetupLinuxCi by tasks.registering {
    group = "mpv-multi"
    description = "Cross-OS: build a real libmpv.so.2 in a container and stage it with its .so closure."
    val outputDir = rootDir.resolve("mpv-natives/linux-x64/")
    val dockerDir = rootDir.resolve("scripts/mpv-linux")
    inputs.dir(dockerDir)
    inputs.property("mpvVersion", mpvVersion)
    outputs.dir(outputDir)
    doLast {
        if (!toolAvailable("docker")) {
            logger.warn(
                "[mpv-multi] Skipping the Linux slice: docker is not on PATH. " +
                    "The other slices are unaffected.",
            )
            return@doLast
        }

        val tag = "simpmusic-libmpv:$mpvVersion"
        logger.lifecycle("[mpv-multi] Building $tag (libplacebo + FFmpeg + mpv from source, ~20-40 min cold)")
        runChecked("docker", "build", "-t", tag, dockerDir.absolutePath)

        val container = runCapturing("docker", "create", tag)
        check(container.isNotEmpty()) { "docker create returned no container id" }
        try {
            outputDir.deleteRecursively()
            outputDir.mkdirs()
            runChecked("docker", "cp", "$container:/out/.", outputDir.absolutePath)
        } finally {
            runChecked("docker", "rm", container)
        }

        val staged = outputDir.resolve("libmpv.so.2")
        check(staged.isFile) { "libmpv.so.2 missing from the container output" }
        val elfType =
            staged.inputStream().use { stream ->
                val header = ByteArray(18)
                check(stream.read(header) == header.size) { "libmpv.so.2 is truncated" }
                (header[0x10].toInt() and 0xFF) or ((header[0x11].toInt() and 0xFF) shl 8)
            }
        check(elfType == 3) { "libmpv.so.2 is not ET_DYN (e_type=$elfType) — it cannot be dlopen()ed" }

        val libs = outputDir.resolve("lib").listFiles()?.size ?: 0
        logger.lifecycle(
            "[mpv-multi] linux-x64: staged libmpv.so.2 + $libs shared objects " +
                "(${outputDir.walkTopDown().filter { it.isFile }.sumOf { it.length() } / 1048576} MB)",
        )
    }
}

val mpvNativesRepo = "maxrave-dev/simpmusic-files"
val mpvNativesTag = "abc"
val mpvSlices = listOf("linux-x64", "macos-arm64", "macos-x64", "windows-x64", "windows-arm64")

val mpvBundleAll by tasks.registering {
    group = "mpv-bundle"
    description = "Mac only: build every native slice and pack them into build/mpv-dist/ for a GitHub release."
    dependsOn(
        mpvSetupLinuxCi,
        mpvSetupMacArmCi,
        mpvSetupMacX64Ci,
        mpvSetupWindowsX64Ci,
        mpvSetupWindowsArmCi,
    )
    val distDir = layout.buildDirectory.dir("mpv-dist")
    outputs.dir(distDir)
    doLast {
        val dist = distDir.get().asFile
        dist.deleteRecursively()
        dist.mkdirs()
        mpvSlices.forEach { slice ->
            val sliceDir = rootDir.resolve("mpv-natives/$slice")
            check(sliceDir.isDirectory && sliceDir.listFiles()?.isNotEmpty() == true) {
                "mpv-natives/$slice is missing or empty — cannot pack an incomplete set"
            }
            runChecked(
                "tar",
                "-czf",
                dist.resolve("mpv-natives-$slice.tar.gz").absolutePath,
                "-C",
                rootDir.resolve("mpv-natives").absolutePath,
                slice,
            )
        }
        logger.lifecycle("[mpv-bundle] Packed ${mpvSlices.size} slices into ${dist.absolutePath}")
        logger.lifecycle("[mpv-bundle] Paste these into mpvNativesChecksums:")
        mpvSlices.forEach { slice ->
            logger.lifecycle("        \"$slice\" to \"${sha256(dist.resolve("mpv-natives-$slice.tar.gz"))}\",")
        }
        logger.lifecycle("[mpv-bundle] Publish with:")
        logger.lifecycle(
            "  gh release create $mpvNativesTag ${dist.absolutePath}/*.tar.gz " +
                "--repo $mpvNativesRepo --title \"Desktop natives (mpv $mpvVersion)\" --notes \"...\"",
        )
    }
}

val mpvNativesChecksums =
    mapOf(
        "linux-x64" to "55e8118a8c4ef201a3b72a71eb20e8854b04c3793d0c9ea0cd7b17ac77aaeee7",
        "macos-arm64" to "e527daac8f6cc196324ea6f0ce54d119d04450af9795ff2856c1891806c1d5e0",
        "macos-x64" to "95170ea54e1f637fdee148a9efd97993c305a92e233b8478d3764fbecce3eb02",
        "windows-x64" to "256f17cf402c7583b8684d5a7cf585ad1b59695469219671f4887b9d8d272a99",
        "windows-arm64" to "30e04a117de0b7d6abc5f86d4231e9b4bffa3637f282ff9efe3dc66e6cc4fcba",
    )

val mpvSetupAll by tasks.registering {
    group = "mpv-multi"
    description = "Populate mpv-natives/ from the prebuilt release tarballs. Runs anywhere; this is what CI uses."
    val outputRoot = rootDir.resolve("mpv-natives")
    inputs.property("mpvNativesTag", mpvNativesTag)
    inputs.property("mpvNativesChecksums", mpvNativesChecksums)
    outputs.dir(outputRoot)
    doLast {
        val cache = mpvCacheDir.get().asFile
        mpvSlices.forEach { slice ->
            val archive = cache.resolve("mpv-natives-$slice-$mpvNativesTag.tar.gz")
            downloadIfMissing(
                "https://github.com/$mpvNativesRepo/releases/download/$mpvNativesTag/mpv-natives-$slice.tar.gz",
                archive,
                logPrefix = "mpv-multi",
            )
            val expected = mpvNativesChecksums.getValue(slice)
            val actual = sha256(archive)
            check(expected != "PENDING") {
                "No checksum pinned for $slice. Run `:composeApp:mpvBundleAll`, publish the " +
                    "archives, then paste the printed digests into mpvNativesChecksums."
            }
            check(actual == expected) {
                archive.delete()
                "Checksum mismatch for mpv-natives-$slice.tar.gz\n  expected $expected\n  actual   $actual"
            }
            val target = outputRoot.resolve(slice)
            target.deleteRecursively()
            outputRoot.mkdirs()
            runChecked("tar", "-xzf", archive.absolutePath, "-C", outputRoot.absolutePath)
            check(target.isDirectory) { "$slice missing after unpacking ${archive.name}" }

            val appleDouble = target.walkTopDown().filter { it.isFile && it.name.startsWith("._") }.toList()
            appleDouble.forEach { it.delete() }
            if (appleDouble.isNotEmpty()) {
                logger.lifecycle("[mpv-multi] $slice: stripped ${appleDouble.size} AppleDouble sidecars")
            }
        }
        logger.lifecycle("[mpv-multi] Unpacked ${mpvSlices.size} verified native slices into mpv-natives/")
    }
}

buildkonfig {
    packageName = "com.maxrave.simpmusic"
    exposeObjectWithName = "BuildKonfig"
    defaultConfigs {
        val versionName =
            libs.versions.version.name
                .get()
        val versionCode =
            libs.versions.version.code
                .get()
                .toInt()
        buildConfigField(STRING, "versionName", versionName)
        buildConfigField(INT, "versionCode", "$versionCode")

        if (isFullBuild) {
            try {
                println("Full build detected, enabling Sentry DSN")
                val properties = Properties()
                properties.load(rootProject.file("local.properties").inputStream())
                buildConfigField(
                    STRING,
                    "sentryDsn",
                    properties.getProperty("SENTRY_DSN") ?: "",
                )
                buildConfigField(
                    STRING,
                    "lastfmApiKey",
                    properties.getProperty("LASTFM_API_KEY") ?: "",
                )
                buildConfigField(
                    STRING,
                    "lastfmSecret",
                    properties.getProperty("LASTFM_SECRET") ?: "",
                )
            } catch (e: Exception) {
                println("Failed to load secrets from local.properties: ${e.message}")
                buildConfigField(STRING, "sentryDsn", "")
                buildConfigField(STRING, "lastfmApiKey", "")
                buildConfigField(STRING, "lastfmSecret", "")
            }
        } else {
            buildConfigField(STRING, "sentryDsn", "")
            buildConfigField(STRING, "lastfmApiKey", "")
            buildConfigField(STRING, "lastfmSecret", "")
        }
    }
}

aboutLibraries {
    collect.configPath = file("../config")
    export {
        outputFile = file("src/commonMain/composeResources/files/aboutlibraries.json")
        prettyPrint = true
        excludeFields = listOf("generated")
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
        duplicationRule = com.mikepenz.aboutlibraries.plugin.DuplicateRule.SIMPLE
    }
}

afterEvaluate {
    tasks
        .matching { it.name.startsWith("prepare") && it.name.endsWith("ArtProfile") }
        .configureEach {
            dependsOn("generateBuildKonfig")
        }
}
