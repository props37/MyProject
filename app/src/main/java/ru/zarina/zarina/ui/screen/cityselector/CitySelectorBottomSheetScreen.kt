package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect
import ru.zarina.zarina.util.compose.ScreenPlaceholder

@Composable
fun CitySelectorBottomSheetScreen(
    viewModel: CitySelectorViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<SideEffect>,
) {
    ZarinaBottomSheet(windowInsets = WindowInsets.statusBars) {
        ScreenPlaceholder(title = "Выбор города")
    }
}

@Preview
@Composable
private fun Preview() {
    // TODO: [High] Add preview
}

