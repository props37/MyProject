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
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.impl.impl.component.HomeContent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.model.HomeContentState

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
            .background(UiKitTheme.colors.background.general.regular.default),
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
