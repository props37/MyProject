package ru.livetyping.zarina.feature.home.ui.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarHeightAsState
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeEvent
import ru.livetyping.zarina.feature.home.ui.impl.screen.model.HomeState
import ru.livetyping.zarina.feature.home.ui.impl.screen.ui.HomeContent

@Composable
internal fun HomeScreen(
    navActions: HomeFeature.NavActions,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()

    LifecycleEventEffect(onLifecycleEvent = viewModel::onLifecycleEvent)

    ScreenContent(
        homeState = homeState,
        onHomeEvent = viewModel::onHomeEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    homeState: HomeState,
    onHomeEvent: (HomeEvent) -> Unit,
    sideEffects: Flow<HomeSideEffect>,
    navActions: HomeFeature.NavActions,
) {
    HomeScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white),
    ) {
        HomeContent(
            homeState = homeState,
            onHomeEvent = onHomeEvent,
            windowInsetsProvider = { WindowInsets.safeDrawing },
            bottomPaddingProvider = { bottomNavBarHeightAsState().value },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
