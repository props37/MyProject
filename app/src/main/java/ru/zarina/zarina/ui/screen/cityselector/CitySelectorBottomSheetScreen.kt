package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect

@Composable
fun CitySelectorBottomSheetScreen(
    navigateBackward: (CitySelectorScreenResult) -> Unit,
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
        onCloseClicked = viewModel::onCloseClicked,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (CitySelectorScreenResult) -> Unit,
    onCloseClicked: () -> Unit,
) {
    CitySelectorScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    ZarinaBottomSheet(windowInsets = WindowInsets.statusBars) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onCloseClicked = onCloseClicked)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}

