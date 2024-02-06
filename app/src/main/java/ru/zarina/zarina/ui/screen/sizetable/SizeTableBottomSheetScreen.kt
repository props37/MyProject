package ru.zarina.zarina.ui.screen.sizetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.sizetable.SizeTableScreenComponents.SizeTableLabel
import ru.zarina.zarina.ui.screen.sizetable.SizeTableScreenComponents.Sizes
import ru.zarina.zarina.ui.screen.sizetable.SizeTableScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.sizetable.SizeTableViewModel.SideEffect
import ru.zarina.zarina.ui.screen.sizetable.SizeTableViewModel.Size

// TODO: [High] Rename?
@Composable
fun SizeTableBottomSheetScreen(
    navigateBackward: (SizeTableScreenResult) -> Unit,
    viewModel: SizeTableViewModel = hiltViewModel(),
) {
    val sizes by viewModel.sizes.collectAsStateWithLifecycle()

    ScreenContent(
        sizes = sizes,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    sizes: ImmutableList<Size>,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateBackward: (SizeTableScreenResult) -> Unit,
) {
    SizeTableScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
    ) {
        SizeTableLabel(modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        ZarinaBottomSheet {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopBar(
                    onCloseClicked = onCloseClicked,
                    modifier = Modifier.fillMaxWidth(),
                )

                Sizes(sizes = sizes)
            }
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
