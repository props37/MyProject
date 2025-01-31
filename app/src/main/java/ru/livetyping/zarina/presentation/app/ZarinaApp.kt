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
import androidx.navigation.compose.rememberNavController
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.LocalBottomNavBarBehaviorController
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.rememberBottomNavBarBehaviorController
import ru.livetyping.zarina.core.uikit.bottomnavbar.sizetracker.LocalBottomNavBarSizeTracker
import ru.livetyping.zarina.core.uikit.bottomnavbar.sizetracker.rememberBottomNavBarSizeTracker
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController
import ru.livetyping.zarina.core.uikit.toast.ZarinaToastContainer
import ru.livetyping.zarina.core.uikit.toast.rememberZarinaToastController
import androidx.navigation.NavHostController
import ru.livetyping.zarina.presentation.bottomnavbar.LocalBottomNavBarSizeTracker
import ru.livetyping.zarina.presentation.bottomnavbar.ZarinaBottomNavBar
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaBottomSheetDefaults
import ru.livetyping.zarina.presentation.common.media.exoplayer.LocalExoPlayerCacheHolder
import ru.livetyping.zarina.presentation.common.media.exoplayer.rememberExoPlayerCacheHolder
import ru.livetyping.zarina.presentation.common.toastcontroller.LocalToastController
import ru.livetyping.zarina.presentation.common.toastcontroller.rememberToastController
import ru.livetyping.zarina.presentation.feature.Features
import ru.livetyping.zarina.presentation.navigation.ZarinaNavigation
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations

@Composable
fun ZarinaApp(
    features: Features,
    navController: NavHostController,
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val toastController = rememberToastController()

    val defaultBottomNavBarBehavior = remember(viewModel.startFeature) {
        when (viewModel.startFeature) {
            AppStartFeature.ONBOARDING -> BottomNavBarBehavior.Hidden(isAnimated = false)
            AppStartFeature.HOME -> BottomNavBarBehavior.Visible(isAnimated = false)
        }
    }
    val bottomNavBarBehaviorController =
        rememberBottomNavBarBehaviorController(defaultBottomNavBarBehavior)

    val bottomNavBarSizeTracker = rememberBottomNavBarSizeTracker()

    val exoPlayerCacheHolder = rememberExoPlayerCacheHolder(
        cache = viewModel.exoPlayerCache,
        cacheDataSourceFactory = viewModel.exoPlayerCacheDataSourceFactory,
    )

    val zarinaToastController = rememberZarinaToastController()

    CompositionLocalProvider(
        LocalToastController provides toastController,
        LocalBottomNavBarBehaviorController provides bottomNavBarBehaviorController,
        LocalBottomNavBarSizeTracker provides bottomNavBarSizeTracker,
        LocalExoPlayerCacheHolder provides exoPlayerCacheHolder,
        LocalZarinaToastController provides zarinaToastController,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ZarinaToastContainer(
                controller = zarinaToastController,
                shouldPaintStatusBar = true,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .fillMaxWidth(),
            )

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
                        modifier = Modifier.fillMaxSize(),
                    )

                    val wishlistProductCount by viewModel.wishlistProductCount.collectAsStateWithLifecycle()
                    val cartProductCount by viewModel.cartProductCount.collectAsStateWithLifecycle()
                    ZarinaBottomNavBar(
                        navController = navController,
                        wishlistProductCountProvider = { wishlistProductCount },
                        cartProductCountProvider = { cartProductCount },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}
