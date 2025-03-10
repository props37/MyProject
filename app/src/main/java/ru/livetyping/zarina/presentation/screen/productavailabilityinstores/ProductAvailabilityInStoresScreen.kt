package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresScreenComponents.Offers
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresViewModel.OfferItem
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ProductAvailabilityInStoresScreen(
    navigate: (ProductAvailabilityInStoresScreenAction) -> Unit,
    viewModel: ProductAvailabilityInStoresViewModel = hiltViewModel(),
) {
    val offers by viewModel.offers.collectAsStateWithLifecycle()

    ScreenContent(
        offers = offers,
        onOfferClicked = viewModel::onOfferClicked,
        onBackClicked = viewModel::onBackClicked,
        navigate = navigate,
        sideEffects = viewModel.sideEffects,
    )
}

@Composable
private fun ScreenContent(
    offers: ImmutableList<OfferItem>,
    onOfferClicked: (OfferItem) -> Unit,
    onBackClicked: () -> Unit,
    navigate: (ProductAvailabilityInStoresScreenAction) -> Unit,
    sideEffects: Flow<ProductAvailabilityInStoresViewModel.SideEffect>,
) {
    ProductAvailabilityInStoresScreenBehavior(
        navigate = navigate,
        sideEffects = sideEffects,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(onBackClicked = onBackClicked)
        Spacer(modifier = Modifier.height(8.dp))

        if (offers.size > 1) {
            Offers(
                offers = offers,
                onOfferClicked = onOfferClicked,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
