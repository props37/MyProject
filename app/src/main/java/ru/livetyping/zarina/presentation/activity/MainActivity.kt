package ru.livetyping.zarina.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cloud.mindbox.mobile_sdk.Mindbox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.base.behavior.DefaultBehaviorController
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.presentation.activity.lifecycleobserver.ActivityLifecycleObserverManager
import ru.livetyping.zarina.presentation.app.ZarinaApp
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.LocalScreenBrightnessBehaviorController
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.ScreenBrightness
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.ScreenBrightnessBehavior
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.ScreenBrightnessBehaviorController
import ru.livetyping.zarina.presentation.common.behavior.screenbrightness.toWindowManagerBrightness
import ru.livetyping.zarina.presentation.common.behavior.systembars.LocalSystemBarsBehaviorController
import ru.livetyping.zarina.presentation.common.behavior.systembars.SystemBarsBehavior
import ru.livetyping.zarina.presentation.common.behavior.systembars.SystemBarsBehaviorController
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.presentation.theme.ZarinaTheme
import ru.livetyping.zarina.usecase.authorization.GetAuthorizationTokensFlowUseCase
import ru.livetyping.zarina.usecase.cart.FetchCartProductIdsUseCase
import ru.livetyping.zarina.usecase.favorite.FetchFavoriteProductIdsUseCase
import ru.livetyping.zarina.usecase.user.FetchUserCityUseCase
import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.accompanist.rememberBottomSheetNavigator
import ru.livetyping.zarina.util.library.activity.DefaultDarkScrim
import ru.livetyping.zarina.util.library.activity.DefaultLightScrim
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.platform.getSizeInBytes
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var activityLifecycleObserverManager: ActivityLifecycleObserverManager

    @Inject
    lateinit var getAuthorizationTokensFlowUseCase: GetAuthorizationTokensFlowUseCase

    @Inject
    lateinit var fetchCartProductIdsUseCase: FetchCartProductIdsUseCase

    @Inject
    lateinit var fetchFavoriteProductIdsUseCase: FetchFavoriteProductIdsUseCase

    @Inject
    lateinit var fetchUserCityUseCase: FetchUserCityUseCase

    @Inject
    lateinit var getUserFlowUseCase: GetUserFlowUseCase

    private val authTokensFlow by lazy {
        getAuthorizationTokensFlowUseCase()
            .map { it.getOrNull() }
            .conflate()
            .shareIn(
                scope = lifecycleScope,
                started = SharingStarted.WhileUiSubscribed,
                replay = 1,
            )
    }

    private var lastTokensCartProductIdsFetchedFor: AuthorizationTokens? = null
    private var lastTokensFavoriteProductIdsFetchedFor: AuthorizationTokens? = null

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        intent?.let { Mindbox.onPushClicked(this, it) }

        fetchUser()
        fetchUserCity()
        setUpCartProductIdsFetching()
        setUpFavoriteProductIdsFetching()

        addActivityLifecycleObservers()

        val defaultSystemBarsBehavior = SystemBarsBehavior(
            isStatusBarContentLight = false,
            isNavigationBarContentLight = false,
        )
        val systemBarsBehaviorController = DefaultBehaviorController(defaultSystemBarsBehavior)
        applySystemBarsBehavior(systemBarsBehaviorController)

        val defaultScreenBrightnessBehavior = ScreenBrightnessBehavior(ScreenBrightness.DEFAULT)
        val screenBrightnessBehaviorController = DefaultBehaviorController(defaultScreenBrightnessBehavior)
        applyScreenBrightnessBehavior(screenBrightnessBehaviorController)

        setContent {
            CompositionLocalProvider(
                LocalSystemBarsBehaviorController provides systemBarsBehaviorController,
                LocalScreenBrightnessBehaviorController provides screenBrightnessBehaviorController,
            ) {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                SideEffect { this.navController = navController }

                ZarinaTheme {
                    ZarinaApp(
                        navController = navController,
                        bottomSheetNavigator = bottomSheetNavigator,
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Mindbox.onPushClicked(this, intent)
        navController?.handleDeepLink(intent)
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
                controller.currentBehavior.collect { behavior ->
                    val window: Window? = this@MainActivity.window
                    val brightness = behavior.brightness.toWindowManagerBrightness()
                    val layoutParams = window?.attributes
                    layoutParams?.screenBrightness = brightness
                    window?.attributes = layoutParams
                }
            }
        }
    }

    private fun fetchUser() {
        lifecycleScope.launch {
            getUserFlowUseCase().firstOrNull()
        }
        // TODO: [High] Fetch updated user
    }

    private fun fetchUserCity() {
        lifecycleScope.launch {
            fetchUserCityUseCase()
        }
    }

    // TODO: [Top] Test!
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setUpCartProductIdsFetching() {
        authTokensFlow
            .filterNotNull()
            .distinctUntilChanged()
            .transformLatest<AuthorizationTokens, Unit> { tokens ->
                if (tokens != lastTokensCartProductIdsFetchedFor) {
                    // TODO: [High] Find a better way
                    // Delay is used to prevent making requests with old authorization tokens
                    // as tokens stored on the disk get updated earlier than HttpClient tokens
                    delay(FETCHING_DELAY)
                    fetchCartProductIdsUseCase()
                }
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    // TODO: [Top] Test!
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setUpFavoriteProductIdsFetching() {
        authTokensFlow
            .filterNotNull()
            .distinctUntilChanged()
            .transformLatest<AuthorizationTokens, Unit> { tokens ->
                if (tokens != lastTokensFavoriteProductIdsFetchedFor) {
                    // TODO: [High] Find a better way
                    // Delay is used to prevent making requests with old authorization tokens
                    // as tokens stored on the disk get updated earlier than HttpClient tokens
                    delay(FETCHING_DELAY)
                    fetchFavoriteProductIdsUseCase()
                }
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    companion object {
        private val FETCHING_DELAY = 1.seconds
    }
}
