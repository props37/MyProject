package ru.zarina.zarina.ui.screen.sizeselector.heightselector

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
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorScreenComponents.SizeSelectorScaffold
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorScreenComponents.Offers
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorViewModel.SideEffect

@Composable
fun HeightSelectorBottomSheetScreenScreen(
    navigateBackward: (HeightSelectorScreenResult) -> Unit,
    viewModel: HeightSelectorViewModel = hiltViewModel(),
) {
    val offers by viewModel.offers.collectAsStateWithLifecycle()

    ScreenContent(
        offers = offers,
        onOfferClicked = viewModel::onOfferClicked,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    offers: ImmutableList<ProductOffer>,
    onOfferClicked: (ProductOffer) -> Unit,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateBackward: (HeightSelectorScreenResult) -> Unit,
) {
    HeightSelectorScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    SizeSelectorScaffold(
        onClickOutside = onBackClicked,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBar(
                onBackClicked = onBackClicked,
                onCloseClicked = onCloseClicked,
                modifier = Modifier.fillMaxWidth(),
            )

            Offers(
                offers = offers,
                onOfferClicked = onOfferClicked,
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
