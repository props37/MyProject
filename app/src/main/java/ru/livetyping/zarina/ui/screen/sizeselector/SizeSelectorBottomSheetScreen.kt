package ru.livetyping.zarina.ui.screen.sizeselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.SizeSelectorScaffold
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.Sizes
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorViewModel.SideEffect
import ru.livetyping.zarina.ui.screen.sizeselector.SizeSelectorViewModel.Size

@Composable
fun SizeSelectorBottomSheetScreen(
    navigate: (SizeSelectorScreenAction) -> Unit,
    viewModel: SizeSelectorViewModel = hiltViewModel(),
) {
    val sizes by viewModel.sizes.collectAsStateWithLifecycle()

    ScreenContent(
        sizes = sizes,
        onSizeClicked = viewModel::onSizeClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    sizes: ImmutableList<Size>,
    onSizeClicked: (Size) -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (SizeSelectorScreenAction) -> Unit,
) {
    SizeSelectorScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
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
        val sizes = remember {
            persistentListOf(
                Size(size = "2XS 40", offers = FakeDataGenerator.getProductOffers(2).toImmutableList()),
                Size(size = "XS 42", offers = FakeDataGenerator.getProductOffers(2).toImmutableList()),
                Size(size = "S 44", offers = FakeDataGenerator.getProductOffers(2).toImmutableList()),
                Size(size = "M 46", offers = FakeDataGenerator.getProductOffers(2).toImmutableList()),
                Size(size = "L 48", offers = FakeDataGenerator.getProductOffers(2).toImmutableList()),
            )
        }

        ScreenContent(
            sizes = sizes,
            onSizeClicked = {},
            onCloseClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
