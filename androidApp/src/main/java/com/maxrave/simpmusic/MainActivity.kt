package com.maxrave.simpmusic

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.eygraber.uri.toKmpUriOrNull
import com.maxrave.common.FIRST_TIME_MIGRATION
import com.maxrave.common.SELECTED_LANGUAGE
import com.maxrave.common.STATUS_DONE
import com.maxrave.common.SUPPORTED_LANGUAGE
import com.maxrave.common.SUPPORTED_LOCATION
import com.maxrave.domain.data.model.intent.GenericIntent
import com.maxrave.domain.manager.DataStoreManager
import com.maxrave.domain.mediaservice.handler.MediaPlayerHandler
import com.maxrave.domain.mediaservice.handler.ToastType
import com.maxrave.logger.Logger
import com.maxrave.media3.di.setServiceActivitySession
import com.maxrave.simpmusic.di.viewModelModule
import com.maxrave.simpmusic.service.rss.RssFeedNotifyWork
import com.maxrave.simpmusic.service.test.notification.NotifyWork
import com.maxrave.simpmusic.utils.ComposeResUtils
import com.maxrave.simpmusic.utils.VersionManager
import com.maxrave.simpmusic.viewModel.SharedViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import org.simpmusic.crashlytics.pushPlayerError
import pub.devrel.easypermissions.EasyPermissions
import java.util.Locale
import java.util.concurrent.TimeUnit

// ---> Naye Firebase Imports <---
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    val viewModel: SharedViewModel by inject()
    val mediaPlayerHandler by inject<MediaPlayerHandler>()
    val dataStoreManager: DataStoreManager by inject()

    private var mBound = false
    private var shouldUnbind = false
    private val serviceConnection =
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?,
            ) {
                setServiceActivitySession(this@MainActivity, MainActivity::class.java, service)
                Logger.w("MainActivity", "onServiceConnected: ")
                mBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Logger.w("MainActivity", "onServiceDisconnected: ")
                mBound = false
            }
        }

    override fun onStart() {
        super.onStart()
        startMusicService()
    }

    override fun onStop() {
        super.onStop()
        if (shouldUnbind) {
            unbindService(serviceConnection)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Logger.d("MainActivity", "onNewIntent: $intent")
        viewModel.setIntent(
            GenericIntent(
                action = intent.action,
                data = (intent.data ?: intent.getStringExtra(Intent.EXTRA_TEXT)?.toUri())?.toKmpUriOrNull(),
                type = intent.type,
            ),
        )
    }

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ---> FIREBASE REMOTE CONFIG CODE START <---
        try {
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0) // Testing ke liye 0 seconds
                .build()
            remoteConfig.setConfigSettingsAsync(configSettings)

            remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 1. Maintenance Check
                    val isMaintenance = remoteConfig.getBoolean("maintenance_mode")
                    if (isMaintenance) {
                        runOnUiThread {
                            android.app.AlertDialog.Builder(this)
                                .setTitle("Maintenance Notice 🚧")
                                .setMessage("App par abhi kaam chal raha hai. Kripya thodi der baad koshish karein.")
                                .setCancelable(false)
                                .show()
                        }
                        return@addOnCompleteListener
                    }

                    // 2. Version Update Check
                    val latestVersionCode = remoteConfig.getString("latest_version_code")
                    val updateUrl = remoteConfig.getString("update_url")
                    val currentVersion = "1.7.0" // Aapka current version

                    if (latestVersionCode.isNotEmpty() && latestVersionCode != currentVersion) {
                        runOnUiThread {
                            android.app.AlertDialog.Builder(this)
                                .setTitle("Naya Update Available! 🚀")
                                .setMessage("App ka naya version ($latestVersionCode) aa gaya hai. Update karein!")
                                .setPositiveButton("Update") { _, _ ->
                                    if (updateUrl.isNotEmpty()) {
                                        val intent = android.content.Intent(
                                            android.content.Intent.ACTION_VIEW, 
                                            android.net.Uri.parse(updateUrl)
                                        )
                                        startActivity(intent)
                                    }
                                }
                                .setCancelable(false)
                                .show()
                        }
                        return@addOnCompleteListener
                    }

                    // 3. Custom Announcement Pop-up Check
                    val showAnnouncement = remoteConfig.getBoolean("show_announcement")
                    val announcementMsg = remoteConfig.getString("announcement_msg")

                    if (showAnnouncement && announcementMsg.isNotEmpty()) {
                        runOnUiThread {
                            android.app.AlertDialog.Builder(this)
                                .setTitle("Notice 📢")
                                .setMessage(announcementMsg)
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Logger.e("FirebaseConfig", "Firebase setup error: ${e.message}")
        }
        // ---> FIREBASE REMOTE CONFIG CODE END <---

        loadKoinModules(
            module {
                single<AppCompatActivity> { this@MainActivity }
            },
        )
        unloadKoinModules(viewModelModule)
        loadKoinModules(viewModelModule)
        VersionManager.initialize()
        checkForUpdate()
        if (viewModel.recreateActivity.value || viewModel.isServiceRunning) {
            viewModel.activityRecreateDone()
        } else {
            startMusicService()
        }
        Logger.d("MainActivity", "onCreate: ")
        val data = (intent?.data ?: intent?.getStringExtra(Intent.EXTRA_TEXT)?.toUri())?.toKmpUriOrNull()
        if (data != null) {
            viewModel.setIntent(
                GenericIntent(
                    action = intent.action,
                    data = data,
                    type = intent.type,
                ),
            )
        }
        Logger.d("Italy", "Key: ${Locale.ITALY.toLanguageTag()}")

        if (getString(FIRST_TIME_MIGRATION) != STATUS_DONE) {
            Logger.d("Locale Key", "onCreate: ${Locale.getDefault().toLanguageTag()}")
            if (SUPPORTED_LANGUAGE.codes.contains(Locale.getDefault().toLanguageTag())) {
                putString(SELECTED_LANGUAGE, Locale.getDefault().toLanguageTag())
                if (SUPPORTED_LOCATION.items.contains(Locale.getDefault().country)) {
                    putString("location", Locale.getDefault().country)
                } else {
                    putString("location", "US")
                }
            } else {
                putString(SELECTED_LANGUAGE, "en-US")
            }
            getString(SELECTED_LANGUAGE)?.let {
                Logger.d("Locale Key", "getString: $it")
                val localeList = LocaleListCompat.forLanguageTags(it)
                AppCompatDelegate.setApplicationLocales(localeList)
                putString(FIRST_TIME_MIGRATION, STATUS_DONE)
            }
        }
        if (AppCompatDelegate.getApplicationLocales().toLanguageTags() !=
            getString(SELECTED_LANGUAGE)
        ) {
            putString(SELECTED_LANGUAGE, AppCompatDelegate.getApplicationLocales().toLanguageTags())
        }

        enableEdgeToEdge(
            navigationBarStyle =
                SystemBarStyle.dark(
                    scrim = Color.Transparent.toArgb(),
                ),
            statusBarStyle =
                SystemBarStyle.dark(
                    scrim = Color.Transparent.toArgb(),
                ),
        )
        viewModel.checkIsRestoring()
        val request =
            PeriodicWorkRequestBuilder<NotifyWork>(
                12L,
                TimeUnit.HOURS,
            ).addTag("Worker Test")
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build(),
                ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "Artist Worker",
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
        lifecycleScope.launch {
            dataStoreManager.blogNotificationEnabled.collect { enabled ->
                if (enabled == DataStoreManager.TRUE) {
                    val rssRequest =
                        PeriodicWorkRequestBuilder<RssFeedNotifyWork>(
                            24L,
                            TimeUnit.HOURS,
                        ).addTag("Blog RSS Worker")
                            .setConstraints(
                                Constraints
                                    .Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build(),
                            ).build()
                    WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
                        "Blog RSS Worker",
                        ExistingPeriodicWorkPolicy.KEEP,
                        rssRequest,
                    )
                } else {
                    WorkManager.getInstance(this@MainActivity).cancelUniqueWork("Blog RSS Worker")
                }
            }
        }

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.POST_NOTIFICATIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val doNotAsk = getString("notification_permission_do_not_ask")
                if (doNotAsk != "true") {
                    val wasAsked = getString("notification_permission_asked")
                    if (wasAsked != "true") {
                        EasyPermissions.requestPermissions(
                            this,
                            runBlocking { ComposeResUtils.getResString(ComposeResUtils.StringType.NOTIFICATION_REQUEST) },
                            1,
                            Manifest.permission.POST_NOTIFICATIONS,
                        )
                        putString("notification_permission_asked", "true")
                    } else {
                        viewModel.showNotificationPermissionDialog()
                    }
                }
            }
        }
        viewModel.getLocation()

        setContent {
            App(viewModel)
        }
    }

    override fun onDestroy() {
        val shouldStopMusicService = viewModel.shouldStopMusicService()
        Logger.w("MainActivity", "onDestroy: Should stop service $shouldStopMusicService")
        if (shouldStopMusicService && shouldUnbind && isFinishing) {
            viewModel.isServiceRunning = false
        }
        unloadKoinModules(viewModelModule)
        super.onDestroy()
        Logger.d("MainActivity", "onDestroy: ")
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.activityRecreate()
    }

    private fun startMusicService() {
        com.maxrave.media3.di
            .startService(this@MainActivity, serviceConnection)
        mediaPlayerHandler.pushPlayerError = { it ->
            pushPlayerError(it)
        }
        mediaPlayerHandler.showToast = { type ->
            viewModel.makeToast(
                when (type) {
                    is ToastType.ExplicitContent -> {
                        runBlocking { ComposeResUtils.getResString(ComposeResUtils.StringType.EXPLICIT_CONTENT_BLOCKED) }
                    }

                    is ToastType.PlayerError -> {
                        runBlocking { ComposeResUtils.getResString(ComposeResUtils.StringType.TIME_OUT_ERROR, type.error) }
                    }
                },
            )
        }
        viewModel.isServiceRunning = true
        shouldUnbind = true
        Logger.d("Service", "Service started")
    }

    private fun checkForUpdate() {
        if (viewModel.shouldCheckForUpdate()) {
            viewModel.checkForUpdate()
        }
    }

    private fun putString(
        key: String,
        value: String,
    ) {
        viewModel.putString(key, value)
    }

    private fun getString(key: String): String? = viewModel.getString(key)

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.activityRecreate()
    }
}
