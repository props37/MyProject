package ru.zarina.zarina.ui.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.cart.CartSize
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.base.rememberErrorState
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.CartContent
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.ProductCardActions
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.cart.CartViewModel.CartState
import ru.zarina.zarina.ui.screen.cart.CartViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.Crossfade

// TODO: [High] Add pull refresh

@Composable
fun CartScreen(
    navigate: (CartScreenAction) -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val cartSize by viewModel.cartSize.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val isClearCartButtonVisible by viewModel.isClearCartButtonVisible.collectAsStateWithLifecycle()
    val deliveryTypes by viewModel.deliveryTypes.collectAsStateWithLifecycle()
    val currentDeliveryType by viewModel.currentDeliveryType.collectAsStateWithLifecycle()
    val deliveryTypeToCartState by viewModel.deliveryTypeToCartState.collectAsStateWithLifecycle()
    val productCardActions = remember(viewModel) {
        ProductCardActions(
            onCountClicked = viewModel::onProductCountClicked,
            onAddToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
            onDeleteFromCartClicked = viewModel::onDeleteProductFromCartClicked,
        )
    }

    ScreenContent(
        cartSize = cartSize,
        city = city,
        onCityClicked = viewModel::onCityClicked,
        onGoToCatalogClicked = viewModel::onGoToCatalogClicked,
        isClearCartButtonVisible = isClearCartButtonVisible,
        onClearCartClicked = viewModel::onClearCartClicked,
        deliveryTypes = deliveryTypes,
        currentDeliveryType = currentDeliveryType,
        onDeliveryTypeChanged = viewModel::onDeliveryTypeChanged,
        deliveryTypeToCartState = deliveryTypeToCartState,
        productCardActions = productCardActions,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    cartSize: CartSize,
    city: City?,
    onCityClicked: () -> Unit,
    onGoToCatalogClicked: () -> Unit,
    isClearCartButtonVisible: Boolean,
    onClearCartClicked: () -> Unit,
    deliveryTypes: ImmutableList<DeliveryType>,
    currentDeliveryType: DeliveryType,
    onDeliveryTypeChanged: (DeliveryType) -> Unit,
    deliveryTypeToCartState: ImmutableMap<DeliveryType, StateFlow<CartState>>,
    productCardActions: ProductCardActions,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CartScreenAction) -> Unit,
) {
    CartScreenBehavior(
        onScreenOpened = onScreenOpened,
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
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            isClearButtonVisible = isClearCartButtonVisible,
            onClearClicked = onClearCartClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        Crossfade(
            targetState = cartSize.isEmpty,
            modifier = Modifier.fillMaxSize(),
        ) { isCartEmpty ->
            if (!isCartEmpty) {
                CartContent(
                    city = city,
                    onCityClicked = onCityClicked,
                    cartSize = cartSize,
                    deliveryTypes = deliveryTypes,
                    currentDeliveryType = currentDeliveryType,
                    onDeliveryTypeChanged = onDeliveryTypeChanged,
                    deliveryTypeToCartState = deliveryTypeToCartState,
                    productCardActions = productCardActions,
                    onGoToCatalogClicked = onGoToCatalogClicked,
                )
            } else {
                val errorState = rememberErrorState(
                    iconResId = R.drawable.ic_cart_outline_64,
                    title = stringResource(R.string.cart_screen_empty_cart_placeholder_title),
                    body = stringResource(R.string.cart_screen_empty_cart_placeholder_description),
                    buttonText = stringResource(R.string.go_to_catalog),
                )
                ZarinaErrorScreen(
                    state = errorState,
                    onButtonClicked = onGoToCatalogClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
