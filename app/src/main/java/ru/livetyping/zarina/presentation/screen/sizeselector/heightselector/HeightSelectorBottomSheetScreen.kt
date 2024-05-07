package ru.livetyping.zarina.presentation.screen.sizeselector.heightselector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.sizeselector.SizeSelectorScreenComponents.SizeSelectorScaffold
import ru.livetyping.zarina.presentation.screen.sizeselector.heightselector.HeightSelectorScreenComponents.Offers
import ru.livetyping.zarina.presentation.screen.sizeselector.heightselector.HeightSelectorScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.sizeselector.heightselector.HeightSelectorViewModel.SideEffect

@Composable
fun HeightSelectorBottomSheetScreenScreen(
    navigate: (HeightSelectorScreenAction) -> Unit,
    viewModel: HeightSelectorViewModel = hiltViewModel(),
) {
    val offers by viewModel.offers.collectAsStateWithLifecycle()

    ScreenContent(
        offers = offers,
        onOfferClicked = viewModel::onOfferClicked,
        onBackClicked = viewModel::onBackClicked,
        onCloseClicked = viewModel::onCloseClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    offers: ImmutableList<ProductOffer>,
    onOfferClicked: (ProductOffer) -> Unit,
    onBackClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (HeightSelectorScreenAction) -> Unit,
) {
    HeightSelectorScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
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
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            offers = remember { FakeDataGenerator.getProductOffers(2).toImmutableList() },
            onOfferClicked = {},
            onBackClicked = {},
            onCloseClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
