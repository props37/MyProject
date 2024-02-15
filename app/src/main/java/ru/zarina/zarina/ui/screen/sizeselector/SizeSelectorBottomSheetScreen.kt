package ru.zarina.zarina.ui.screen.sizeselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.SizeSelectorScaffold
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.Sizes
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorViewModel.SideEffect
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorViewModel.Size

@Composable
fun SizeSelectorBottomSheetScreen(
    navigateForward: (SizeSelectorScreenAction) -> Unit,
    navigateBackward: (SizeSelectorScreenResult) -> Unit,
    viewModel: SizeSelectorViewModel = hiltViewModel(),
) {
    val sizes by viewModel.sizes.collectAsStateWithLifecycle()

    ScreenContent(
        sizes = sizes,
        onSizeClicked = viewModel::onSizeClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    sizes: ImmutableList<Size>,
    onSizeClicked: (Size) -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateForward: (SizeSelectorScreenAction) -> Unit,
    navigateBackward: (SizeSelectorScreenResult) -> Unit,
) {
    SizeSelectorScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
        navigateBackward = navigateBackward,
    )

    SizeSelectorScaffold(
        onClickOutside = onCloseClicked,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBar(
                onCloseClicked = onCloseClicked,
                modifier = Modifier.fillMaxWidth(),
            )

            Sizes(
                sizes = sizes,
                onSizeClicked = onSizeClicked,
            )
        }
    }
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
