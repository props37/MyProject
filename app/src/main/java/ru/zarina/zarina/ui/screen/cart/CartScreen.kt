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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.City
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.DeliveryTypePicker
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.EmptyCartPlaceholder
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.cart.CartViewModel.SideEffect
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun CartScreen(
    navigate: (CartScreenAction) -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val city by viewModel.city.collectAsStateWithLifecycle()
    val isClearCartButtonVisible by viewModel.isClearCartButtonVisible.collectAsStateWithLifecycle()
    val deliveryTypes by viewModel.deliveryTypes.collectAsStateWithLifecycle()
    val currentDeliveryType by viewModel.currentDeliveryType.collectAsStateWithLifecycle()

    ScreenContent(
        city = city,
        onCityClicked = viewModel::onCityClicked,
        onGoToCatalogClicked = viewModel::onGoToCatalogClicked,
        isClearCartButtonVisible = isClearCartButtonVisible,
        onClearCartClicked = viewModel::onClearCartClicked,
        deliveryTypes = deliveryTypes,
        currentDeliveryType = currentDeliveryType,
        onDeliveryTypeClicked = viewModel::onDeliveryTypeClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    city: City?,
    onCityClicked: () -> Unit,
    onGoToCatalogClicked: () -> Unit,
    isClearCartButtonVisible: Boolean,
    onClearCartClicked: () -> Unit,
    deliveryTypes: ImmutableList<DeliveryType>,
    currentDeliveryType: DeliveryType,
    onDeliveryTypeClicked: (DeliveryType) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CartScreenAction) -> Unit,
) {
    CartScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
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

        City(
            city = city,
            onClick = onCityClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        DeliveryTypePicker(
            types = deliveryTypes,
            currentType = currentDeliveryType,
            onTypeClicked = onDeliveryTypeClicked,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        EmptyCartPlaceholder(
            onGoToCatalogClicked = onGoToCatalogClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
