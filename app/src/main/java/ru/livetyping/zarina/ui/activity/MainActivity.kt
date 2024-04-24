package ru.livetyping.zarina.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.base.behavior.DefaultBehaviorController
import ru.livetyping.zarina.ui.activity.lifecycleobserver.ActivityLifecycleObserverManager
import ru.livetyping.zarina.ui.app.ZarinaApp
import ru.livetyping.zarina.ui.common.behavior.systembars.LocalSystemBarsBehaviorController
import ru.livetyping.zarina.ui.common.behavior.systembars.SystemBarsBehavior
import ru.livetyping.zarina.ui.common.behavior.systembars.SystemBarsBehaviorController
import ru.livetyping.zarina.ui.theme.ZarinaTheme
import ru.livetyping.zarina.util.library.activity.DefaultDarkScrim
import ru.livetyping.zarina.util.library.activity.DefaultLightScrim
import ru.livetyping.zarina.util.platform.getSizeInBytes
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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
