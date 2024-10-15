package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.gender.GenderSelectorState
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentEvent
import ru.livetyping.zarina.feature.home.ui.impl.impl.homecontent.HomeContentState

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val genderSelectorState by viewModel.genderSelectorState.collectAsStateWithLifecycle()
    val homeContentState by viewModel.homeContentState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    ScreenContent(
        genderSelectorState = genderSelectorState,
        onGenderSelectorEvent = viewModel::onGenderSelectorEvent,
        homeContentState = homeContentState,
        onHomeContentEvent = viewModel::onHomeContentEvent,
        isRefreshing = isRefreshing,
    )
}

@Composable
private fun ScreenContent(
    genderSelectorState: GenderSelectorState,
    onGenderSelectorEvent: (GenderSelectorEvent) -> Unit,
    homeContentState: HomeContentState,
    onHomeContentEvent: (HomeContentEvent) -> Unit,
    isRefreshing: Boolean,
) {
    // TODO: [Top] Implement
}
