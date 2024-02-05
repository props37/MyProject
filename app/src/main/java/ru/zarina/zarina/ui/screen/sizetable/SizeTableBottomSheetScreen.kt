package ru.zarina.zarina.ui.screen.sizetable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.sizetable.SizeTableScreenComponents.SizeTableLabel
import ru.zarina.zarina.ui.screen.sizetable.SizeTableViewModel.SideEffect

@Composable
fun SizeTableBottomSheetScreen(
    viewModel: SizeTableViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<SideEffect>,
) {
    SizeTableScreenBehavior(sideEffects = sideEffects)

    // TODO: [High] Handle insets
    // TODO: [High] Handle status bar inset?
    Column(modifier = Modifier.fillMaxWidth()) {
        SizeTableLabel(modifier = Modifier.padding(start = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        ZarinaBottomSheet(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.height(200.dp))
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
