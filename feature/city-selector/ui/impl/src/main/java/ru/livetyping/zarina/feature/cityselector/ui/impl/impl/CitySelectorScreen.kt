package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorEvent
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model.CitySelectorState
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui.CityList
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui.SearchTextField
import ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui.TopBar

// TODO: [Top] Request search text field focus automatically

@Composable
internal fun CitySelectorScreen(
    navActions: CitySelectorFeature.NavActions,
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
private fun ScreenContent(
    citySelectorState: CitySelectorState,
    onCitySelectorEvent: (CitySelectorEvent) -> Unit,
    sideEffects: Flow<CitySelectorSideEffect>,
    navActions: CitySelectorFeature.NavActions,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .statusBarsPadding()
            .displayCutoutPadding(),
    ) {
        TopBar(onCloseClicked = { onCitySelectorEvent(CitySelectorEvent.CloseClicked) })

        Spacer(modifier = Modifier.height(8.dp))

        SearchTextField(
            state = citySelectorState.citySearchTextFieldState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        )

        val safeDrawingBottomPadding =
            WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

        CityList(
            state = citySelectorState.cityListState,
            onCitySelectorEvent = onCitySelectorEvent,
            topPadding = 16.dp,
            bottomPadding = safeDrawingBottomPadding,
        )
    }
}
