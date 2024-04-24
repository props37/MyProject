package ru.livetyping.zarina.ui.screen.shops

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview

@Composable
fun ShopsScreen(
    navigate: (ShopsScreenAction) -> Unit,
    viewModel: ShopsViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<ShopsViewModel.SideEffect>,
    navigate: (ShopsScreenAction) -> Unit,
) {
    ShopsScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )


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
