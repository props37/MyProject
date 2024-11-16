package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorState

@Composable
internal fun CitySelectorScreen(
    navActions: CitySelectorNavActions,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    val citySelectorState by viewModel.citySelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        citySelectorState = citySelectorState,
        onCitySelectorEvent = viewModel::onCitySelectorEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    citySelectorState: CitySelectorState,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
    sideEffects: Flow<CitySelectorSideEffect>,
    navActions: CitySelectorNavActions,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(
            title = citySelectorState.title,
            onBackClicked = { onCitySelectorEvent(CitySelectorEvent.BackClicked) },
        )

        // TODO: [Top] Implement
    }
}
