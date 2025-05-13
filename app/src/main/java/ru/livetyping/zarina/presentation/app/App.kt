package ru.livetyping.zarina.presentation.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import ru.livetyping.zarina.core.analytics.compose.LocalAppMetrica
import ru.livetyping.zarina.core.mediacompose.LocalExoPlayerCacheDataSourceFactoryProvider
import ru.livetyping.zarina.core.mediacompose.rememberExoPlayerCacheDataSourceFactoryProvider
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.LocalBottomNavBarBehaviorController
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.rememberBottomNavBarBehaviorController
import ru.livetyping.zarina.core.uikit.bottombar.navigation.sizetracker.LocalBottomNavBarSizeTracker
import ru.livetyping.zarina.core.uikit.bottombar.navigation.sizetracker.rememberBottomNavBarSizeTracker
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaBottomSheetDefaults
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController2
import ru.livetyping.zarina.core.uikit.toast.ZarinaToastContainer2
import ru.livetyping.zarina.core.uikit.toast.rememberZarinaToastController
import ru.livetyping.zarina.core.uikit.toast.rememberZarinaToastController2
import ru.livetyping.zarina.feature.Features
import ru.livetyping.zarina.presentation.bottomnavbar.ZarinaBottomNavBar
import ru.livetyping.zarina.presentation.navigation.ZarinaNavigation

@Composable
fun App(
    features: Features,
    navController: NavHostController,
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val defaultBottomNavBarBehavior = remember(viewModel.startFeature) {
        when (viewModel.startFeature) {
            AppStartFeature.ONBOARDING -> BottomNavBarBehavior.Hidden(isAnimated = false)
            AppStartFeature.HOME -> BottomNavBarBehavior.Visible(isAnimated = false)
        }
    }
    val bottomNavBarBehaviorController =
        rememberBottomNavBarBehaviorController(defaultBottomNavBarBehavior)

    val bottomNavBarSizeTracker = rememberBottomNavBarSizeTracker()

    val exoPlayerCacheDataSourceFactoryProvider = rememberExoPlayerCacheDataSourceFactoryProvider(
        cacheDataSourceFactory = viewModel.exoPlayerCacheDataSourceFactory,
    )

    val zarinaToastController = rememberZarinaToastController()
    val zarinaToastController2 = rememberZarinaToastController2()

    CompositionLocalProvider(
        LocalBottomNavBarBehaviorController provides bottomNavBarBehaviorController,
        LocalBottomNavBarSizeTracker provides bottomNavBarSizeTracker,
        LocalExoPlayerCacheDataSourceFactoryProvider provides exoPlayerCacheDataSourceFactoryProvider,
        LocalAppMetrica provides viewModel.appMetrica,
        LocalZarinaToastController provides zarinaToastController,
        LocalZarinaToastController2 provides zarinaToastController2,
    ) {
        val hazeState = rememberHazeState()

        Box(modifier = Modifier.fillMaxSize()) {
            ZarinaToastContainer2(
                controller = zarinaToastController2,
                hazeState = hazeState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .fillMaxWidth(),
            )

            // TODO: [High] Add support for sheetGesturesEnabled override
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = RectangleShape,
                sheetElevation = 0.dp,
                sheetBackgroundColor = Color.Unspecified,
                sheetContentColor = Color.Unspecified,
                scrimColor = ZarinaBottomSheetDefaults.ScrimColor,
            ) {
                Box(modifier = modifier) {
                    ZarinaNavigation(
                        features = features,
                        navController = navController,
                        startFeature = viewModel.startFeature,
                        modifier = Modifier
                            .fillMaxSize()
                            .hazeSource(hazeState),
                    )

                    val wishlistProductCount by viewModel.wishlistProductCount.collectAsStateWithLifecycle()
                    val cartProductCount by viewModel.cartProductCount.collectAsStateWithLifecycle()
                    ZarinaBottomNavBar(
                        navController = navController,
                        wishlistProductCountProvider = { wishlistProductCount },
                        cartProductCountProvider = { cartProductCount },
                        hazeState = hazeState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}
