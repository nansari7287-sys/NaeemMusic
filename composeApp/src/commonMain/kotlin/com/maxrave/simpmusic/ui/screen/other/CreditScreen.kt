package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maxrave.simpmusic.expect.openUrl
import com.maxrave.simpmusic.ui.theme.typo
import com.maxrave.simpmusic.utils.VersionManager
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import simpmusic.composeapp.generated.resources.Res
import simpmusic.composeapp.generated.resources.app_icon
import simpmusic.composeapp.generated.resources.version_format

// ------------------------------------------------------------------------
// DATA STRUCTURES FOR APP CREDITS & ARCHITECTURE
// ------------------------------------------------------------------------

data class DeveloperProfile(
    val name: String,
    val role: String,
    val url: String,
    val symbol: String,
)

data class OpenSourceLibrary(
    val name: String,
    val author: String,
    val license: String,
    val url: String,
)

data class SocialLink(
    val platform: String,
    val url: String,
    val symbol: String,
)

// ------------------------------------------------------------------------
// DATA REPOSITORY COLLECTIONS
// ------------------------------------------------------------------------

val developerTeam = listOf(
    DeveloperProfile(
        name = "DrakoXNaeem",
        role = "Lead System Architect & Developer",
        url = "https://magma-portfolio-sigma.vercel.app",
        symbol = "👑",
    ),
    DeveloperProfile(
        name = "Maxrave",
        role = "Original SimpMusic Base Creator",
        url = "https://github.com/maxrave-dev",
        symbol = "⭐",
    ),
)

val usedLibraries = listOf(
    OpenSourceLibrary(
        name = "Compose Multiplatform",
        author = "JetBrains",
        license = "Apache License 2.0",
        url = "https://www.jetbrains.com/lp/compose-multiplatform/",
    ),
    OpenSourceLibrary(
        name = "Haze (Glassmorphism Effects)",
        author = "Chris Banes",
        license = "Apache License 2.0",
        url = "https://github.com/chrisbanes/haze",
    ),
    OpenSourceLibrary(
        name = "Ktor Network Framework",
        author = "JetBrains",
        license = "Apache License 2.0",
        url = "https://ktor.io/",
    ),
    OpenSourceLibrary(
        name = "Coil Image Loader",
        author = "Colin White",
        license = "Apache License 2.0",
        url = "https://coil-kt.github.io/coil/",
    ),
    OpenSourceLibrary(
        name = "Kotlin Coroutines & Flow",
        author = "JetBrains",
        license = "Apache License 2.0",
        url = "https://kotlinlang.org/docs/coroutines-overview.html",
    ),
)

val socialLinks = listOf(
    SocialLink(
        platform = "Official Portfolio Website",
        url = "https://magma-portfolio-sigma.vercel.app",
        symbol = "🌐",
    ),
    SocialLink(
        platform = "GitHub Repositories",
        url = "https://github.com/nansari7287-sys",
        symbol = "💻",
    ),
    SocialLink(
        platform = "Instagram Portfolio",
        url = "https://instagram.com",
        symbol = "📸",
    ),
    SocialLink(
        platform = "Facebook Profile",
        url = "https://facebook.com",
        symbol = "📘",
    ),
)

// ------------------------------------------------------------------------
// MAIN COMPOSABLE SCREEN IMPLEMENTATION
// ------------------------------------------------------------------------

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class,
)
@Composable
fun CreditScreen(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    val hazeState = rememberHazeState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + 80.dp))

            AppHeaderSection()

            Spacer(modifier = Modifier.height(32.dp))

            DeveloperSectionTitle()
            
            developerTeam.forEach { developer ->
                DeveloperItemCard(developer = developer)
            }

            Spacer(modifier = Modifier.height(32.dp))

            SocialLinksSectionTitle()
            
            socialLinks.forEach { link ->
                SocialLinkItemCard(socialLink = link)
            }

            Spacer(modifier = Modifier.height(32.dp))

            OpenSourceSectionTitle()
            
            usedLibraries.forEach { library ->
                OpenSourceLibraryCard(library = library)
            }

            Spacer(modifier = Modifier.height(40.dp))

            FooterSection(paddingValues = paddingValues)
        }

        // Custom Top App Bar with Blur Effect
        TopAppBar(
            modifier = Modifier.hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThin(),
            ) {
                blurEnabled = true
            },
            title = {
                Text(
                    text = "Naeem Music - Credits",
                    style = typo().titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            animationMode = MarqueeAnimationMode.Immediately,
                        )
                        .focusable(),
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text(
                        text = "←",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified,
            ),
        )
    }
}

// ------------------------------------------------------------------------
// MODULAR SUB-COMPONENTS
// ------------------------------------------------------------------------

@Composable
fun AppHeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.size(140.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = "Naeem Music App Icon",
                modifier = Modifier.fillMaxSize(),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Naeem Music",
            style = typo().titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(
                Res.string.version_format,
                VersionManager.getVersionName(),
            ),
            style = typo().labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "A high performance professional cross-platform music streaming client engineered with security and precision.",
            style = typo().bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            modifier = Modifier.padding(horizontal = 24.dp),
        )
    }
}

@Composable
fun DeveloperSectionTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "👑", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Developed By",
            style = typo().titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun DeveloperItemCard(developer: DeveloperProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .clickable { openUrl(developer.url) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = developer.symbol, fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = developer.name,
                    style = typo().titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = developer.role,
                    style = typo().bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
fun SocialLinksSectionTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "🌐", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Connect With Us",
            style = typo().titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun SocialLinkItemCard(socialLink: SocialLink) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
    ) {
        TextButton(
            onClick = { openUrl(socialLink.url) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = socialLink.symbol, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = socialLink.platform,
                    style = typo().bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun OpenSourceSectionTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "📦", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Open Source Libraries",
            style = typo().titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun OpenSourceLibraryCard(library: OpenSourceLibrary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .clickable { openUrl(library.url) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
        ) {
            Text(
                text = library.name,
                style = typo().titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "By ${library.author}",
                    style = typo().bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                )
                Text(
                    text = library.license,
                    style = typo().bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                )
            }
        }
    }
}

@Composable
fun FooterSection(paddingValues: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingValues.calculateBottomPadding() + 40.dp),
    ) {
        Text(
            text = "Engineered with Precision & Power",
            style = typo().labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "© 2026 Naeem (DrakoXNaeem). All rights reserved.",
            style = typo().labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "NaeemMusic Advanced Production Build",
            style = typo().labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
        )
    }
}
