package ru.zarina.zarina.ui.screen.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun FiltersScreen(
    viewModel: FiltersViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
) {
    FiltersScreenBehavior(sideEffects = sideEffects)


}

@Preview
@DensityPreviews
@FontScalePreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}

