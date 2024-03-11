package ru.zarina.zarina.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.ui.activity.lifecycleobserver.ActivityLifecycleObserverManager
import ru.zarina.zarina.ui.app.ZarinaApp
import ru.zarina.zarina.ui.common.behavior.base.DefaultBehaviorController
import ru.zarina.zarina.ui.common.behavior.systembars.LocalSystemBarsBehaviorController
import ru.zarina.zarina.ui.common.behavior.systembars.SystemBarsBehavior
import ru.zarina.zarina.ui.common.behavior.systembars.SystemBarsBehaviorController
import ru.zarina.zarina.ui.theme.old.ZarinaTheme
import ru.zarina.zarina.util.library.activity.DefaultDarkScrim
import ru.zarina.zarina.util.library.activity.DefaultLightScrim
import ru.zarina.zarina.util.platform.getSizeInBytes
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var activityLifecycleObserverManager: ActivityLifecycleObserverManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        addActivityLifecycleObservers()

        val defaultSystemBarsBehavior = SystemBarsBehavior(
            isStatusBarContentLight = false,
            isNavigationBarContentLight = false,
        )
        val systemBarsBehaviorController = DefaultBehaviorController(defaultSystemBarsBehavior)

        applySystemBarsBehavior(systemBarsBehaviorController)

        setContent {
            CompositionLocalProvider(
                LocalSystemBarsBehaviorController provides systemBarsBehaviorController,
            ) {
                ZarinaTheme {
                    ZarinaApp()
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (BuildConfig.IS_LOGGING_ENABLED) {
            val savedInstanceStateSize = savedInstanceState.getSizeInBytes()
            Timber.d("onRestoreInstanceState. Size: $savedInstanceStateSize bytes")
        }
    }

    private fun addActivityLifecycleObservers() {
        activityLifecycleObserverManager.observers.forEach { observer ->
            lifecycle.addObserver(observer)
        }
    }

    private fun applySystemBarsBehavior(controller: SystemBarsBehaviorController) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                controller.currentBehavior.collect { behavior ->
                    val statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT,
                        detectDarkMode = { behavior.isStatusBarContentLight },
                    )
                    val navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = SystemBarStyle.DefaultLightScrim,
                        darkScrim = SystemBarStyle.DefaultDarkScrim,
                        detectDarkMode = { behavior.isNavigationBarContentLight },
                    )

                    enableEdgeToEdge(
                        statusBarStyle = statusBarStyle,
                        navigationBarStyle = navigationBarStyle,
                    )
                }
            }
        }
    }
}
