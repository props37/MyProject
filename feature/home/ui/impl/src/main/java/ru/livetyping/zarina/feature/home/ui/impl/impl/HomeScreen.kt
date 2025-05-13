package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentState
import ru.livetyping.zarina.feature.home.ui.impl.impl.ui.HomeContent

@Composable
internal fun HomeScreen(
    navActions: HomeFeature.NavActions,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val genderSelectorState by viewModel.genderSelectorState.collectAsStateWithLifecycle()
    val homeContentState by viewModel.homeContentState.collectAsStateWithLifecycle()

    ScreenContent(
        genderSelectorState = genderSelectorState,
        onGenderSelectorEvent = viewModel::onGenderSelectorEvent,
        homeContentState = homeContentState,
        onHomeContentEvent = viewModel::onHomeContentEvent,
        onLifecycleEvent = viewModel::onLifecycleEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    genderSelectorState: TabRowState<GenderTab>,
    onGenderSelectorEvent: (TabRowEvent<GenderTab>) -> Unit,
    homeContentState: HomeContentState,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<HomeSideEffect>,
    navActions: HomeFeature.NavActions,
) {
    HomeScreenBehavior(
        onLifecycleEvent = onLifecycleEvent,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white),
    ) {
        HomeContent(
            homeContentState = homeContentState,
            onHomeContentEvent = onHomeContentEvent,
            genderSelectorState = genderSelectorState,
            onGenderSelectorEvent = onGenderSelectorEvent,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
