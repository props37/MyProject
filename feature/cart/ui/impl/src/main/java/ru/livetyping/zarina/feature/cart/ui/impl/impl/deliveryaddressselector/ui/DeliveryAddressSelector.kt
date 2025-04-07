package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryAddressSelectorState
import ru.livetyping.zarina.core.resource.R as RCommon

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

            DeliveryOptions(
                state = state.deliveryOptionsState,
                onDeliveryOptionClicked = {
                    onEvent(DeliveryAddressSelectorEvent.DeliveryOptionClicked(it))
                },
                onDeliveryOptionDateClicked = {
                    onEvent(DeliveryAddressSelectorEvent.DeliveryOptionDateClicked(it))
                },
                onDeliveryOptionTimeClicked = {
                    onEvent(DeliveryAddressSelectorEvent.DeliveryOptionTimeClicked(it))
                },
                onDeliveryOptionShowDetailsClicked = {
                    onEvent(DeliveryAddressSelectorEvent.ShowDeliveryOptionDetails(it))
                },
                onErrorRefreshClicked = {
                    onEvent(DeliveryAddressSelectorEvent.ErrorRefreshClicked)
                },
                windowInsetsProvider = windowInsetsProvider,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        ContinueButton(
            isVisible = state.isContinueButtonVisible,
            onClick = { onEvent(DeliveryAddressSelectorEvent.ContinueClicked) },
            windowInsetsProvider = windowInsetsProvider,
        )
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

@Composable
private fun ContinueButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = modifier,
    ) {
        Column {
            ZarinaDivider(modifier = Modifier.fillMaxWidth())
            ZarinaButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .windowInsetsPadding(windowInsetsProvider().only(WindowInsetsSides.Bottom)),
            ) {
                Text(text = stringResource(RCommon.string.res_continue).uppercase())
            }
        }
    }
}
