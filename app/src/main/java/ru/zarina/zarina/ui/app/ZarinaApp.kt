package ru.zarina.zarina.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import ru.zarina.zarina.ui.bottomnavbar.LocalBottomNavBarSizeTracker
import ru.zarina.zarina.ui.bottomnavbar.ZarinaBottomNavBar
import ru.zarina.zarina.ui.bottomnavbar.rememberBottomNavBarSizeTracker
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.rememberBottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.component.toast.ZarinaToastContainer
import ru.zarina.zarina.ui.common.media.exoplayer.LocalExoPlayerCacheHolder
import ru.zarina.zarina.ui.common.media.exoplayer.rememberExoPlayerCacheHolder
import ru.zarina.zarina.ui.common.toastcontroller.LocalToastController
import ru.zarina.zarina.ui.common.toastcontroller.rememberToastController
import ru.zarina.zarina.ui.common.zarinatoastcontroller.LocalZarinaToastController
import ru.zarina.zarina.ui.common.zarinatoastcontroller.rememberZarinaToastController
import ru.zarina.zarina.ui.navigation.ZarinaNavigation
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.theme.Colors
import ru.zarina.zarina.util.library.accompanist.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ZarinaApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    val toastController = rememberToastController()

    val defaultBottomNavBarBehavior = remember(viewModel.startDestination) {
        when (viewModel.startDestination.routeSchema) {
            UnscopedDestinations.Onboarding.routeSchema -> {
                BottomNavBarBehavior.Hidden(isAnimated = false)
            }

            else -> BottomNavBarBehavior.Visible(isAnimated = false)
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
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RectangleShape,
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Unspecified,
            sheetContentColor = Color.Unspecified,
            scrimColor = Colors.MineShaftDark.copy(alpha = 0.4f),
        ) {
            Box(modifier = modifier) {
                ZarinaNavigation(
                    navController = navController,
                    startDestination = viewModel.startDestination,
                    modifier = Modifier.fillMaxSize(),
                )

                val cartProductCount by viewModel.cartProductCount.collectAsStateWithLifecycle()
                ZarinaBottomNavBar(
                    navController = navController,
                    cartProductCount = cartProductCount,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                )

                ZarinaToastContainer(
                    controller = zarinaToastController,
                    shouldPaintStatusBar = true,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                )
            }
        }
    }
}
