package ru.livetyping.zarina.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
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
import ru.livetyping.zarina.ui.common.behavior.screenbrightness.LocalScreenBrightnessBehaviorController
import ru.livetyping.zarina.ui.common.behavior.screenbrightness.ScreenBrightnessBehavior
import ru.livetyping.zarina.ui.common.behavior.screenbrightness.ScreenBrightnessBehaviorController
import ru.livetyping.zarina.ui.common.behavior.systembars.LocalSystemBarsBehaviorController
import ru.livetyping.zarina.ui.common.behavior.systembars.SystemBarsBehavior
import ru.livetyping.zarina.ui.common.behavior.systembars.SystemBarsBehaviorController
import ru.livetyping.zarina.ui.theme.UiKitTheme
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

        val defaultScreenBrightnessBehavior = ScreenBrightnessBehavior(brightness = null)
        val screenBrightnessBehaviorController = DefaultBehaviorController(defaultScreenBrightnessBehavior)
        applyScreenBrightnessBehavior(screenBrightnessBehaviorController)

        setContent {
            CompositionLocalProvider(
                LocalSystemBarsBehaviorController provides systemBarsBehaviorController,
                LocalScreenBrightnessBehaviorController provides screenBrightnessBehaviorController,
            ) {
                ZarinaTheme {
                    ZarinaApp(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(UiKitTheme.colors.background.general.regular.default),
                    )
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

    private fun applyScreenBrightnessBehavior(controller: ScreenBrightnessBehaviorController) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                controller.currentBehavior.collect {
                    val window = this@MainActivity.window

                    val brightness = it.brightness ?: WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                    val layoutParams = window?.attributes
                    layoutParams?.screenBrightness = brightness
                    window?.attributes = layoutParams
                }
            }
        }
    }
}
