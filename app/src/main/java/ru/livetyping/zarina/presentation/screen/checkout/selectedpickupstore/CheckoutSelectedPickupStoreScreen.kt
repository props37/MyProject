package ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.component.ProductOrderCard
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutSelectedPickupStoreScreen(
    navigate: (CheckoutSelectedPickupStoreScreenAction) -> Unit,
    viewModel: CheckoutSelectedPickupStoreViewModel = hiltViewModel(),
) {
    val store by viewModel.store.collectAsStateWithLifecycle()
    val availableProducts by viewModel.availableProducts.collectAsStateWithLifecycle()

    ScreenContent(
        store = store,
        availableProducts = availableProducts,
        onContinueClicked = viewModel::onContinueClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    store: Store,
    availableProducts: List<CartProduct>,
    onContinueClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutSelectedPickupStoreScreenAction) -> Unit,
) {
    CheckoutSelectedPickupStoreScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            store = store,
            onBackClicked = onBackClicked,
        )

        ZarinaItem {
            Text(
                text = stringResource(R.string.these_products_are_available_in_this_store),
                style = UiKitTheme.typography.secondary.bold,
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = Modifier.weight(1f),
        ) {
            itemsIndexed(
                items = availableProducts,
                key = { _, product -> product.productId.value },
            ) { index, product ->
                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl,
                    size = product.size,
                    sizeRu = null,
                    height = product.height,
                    color = product.color,
                    price = product.price,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < availableProducts.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }

        Column {
            ZarinaDivider(modifier = Modifier.fillMaxWidth())

            ZarinaButton(
                onClick = onContinueClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 20.dp)
                    .navigationBarsPadding(),
            ) {
                Text(text = stringResource(R.string.select).uppercase())
            }
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // Add preview
    }
}
