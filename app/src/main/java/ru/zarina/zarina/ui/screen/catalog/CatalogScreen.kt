package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.ScreenPlaceholder
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.SideEffect

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<SideEffect>,
) {
    CatalogScreenBehavior(
        sideEffects = sideEffects,
    )

    ScreenPlaceholder(title = "Каталог")
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [High] Add preview
    }
}
