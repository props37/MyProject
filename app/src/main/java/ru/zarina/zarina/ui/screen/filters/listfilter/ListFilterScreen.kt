package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun ListFilterScreen(
    viewModel: ListFilterViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<ListFilterViewModel.SideEffect>,
) {
    ListFilterScreenBehavior(sideEffects = sideEffects)


}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
