package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState

@Composable
internal fun DeliveryAddressSelector(
    state: DeliveryAddressSelectorState,
    onEvent: (DeliveryAddressSelectorEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Column(modifier = modifier) {
        CityHeader(state.city)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            AddressSelectorBlock(
                streetSelectorTextFieldState = state.streetSelectorTextFieldState,
                buildingSelectorTextFieldState = state.buildingSelectorTextFieldState,
                apartmentSelectorTextFieldState = state.apartmentSelectorTextFieldState,
                isBuildingSelectorClickable = state.isBuildingSelectionEnabled,
                onStreetSelectorClicked = {
                    onEvent(DeliveryAddressSelectorEvent.StreetSelectorClicked)
                },
                onBuildingSelectorClicked = {
                    onEvent(DeliveryAddressSelectorEvent.BuildingSelectorClicked)
                },
            )

            // TODO: [Top] Implement
        }
    }
}

@Composable
private fun CityHeader(
    city: City?,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(modifier = modifier) {
        Crossfade(
            targetState = city,
            contentKey = { it != null },
        ) { city ->
            if (city != null) {
                Text(
                    text = city.name,
                    style = UiKitTheme.typography.secondary.bold,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            } else {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.bold,
                    modifier = Modifier.width(80.dp),
                )
            }
        }
    }
}
