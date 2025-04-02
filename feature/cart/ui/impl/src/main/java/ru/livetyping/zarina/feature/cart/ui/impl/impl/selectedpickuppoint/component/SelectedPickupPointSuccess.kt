package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaRadioButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.model.SelectedPickupPointState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun SelectedPickupPointSuccess(
    state: SelectedPickupPointState.Success,
    onDeliveryTypeClicked: (PickupPointDetailed.DeliveryType) -> Unit,
    onContinueClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            PickupPointInfo(pickupPoint = state.pickupPoint)

            Spacer(modifier = Modifier.height(40.dp))
            PickupPointDeliveryTerms(
                pickupPoint = state.pickupPoint,
                selectedDeliveryTypeId = state.selectedDeliveryTypeId,
                onDeliveryTypeClicked = onDeliveryTypeClicked,
            )

            Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        }

        Column {
            ZarinaDivider(modifier = Modifier.fillMaxWidth())
            ZarinaButton(
                onClick = onContinueClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(text = stringResource(RCommon.string.res_select).uppercase())
            }
            Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
        }
    }
}

@Composable
private fun PickupPointInfo(
    pickupPoint: PickupPointDetailed,
    modifier: Modifier = Modifier,
) {
    val itemModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(modifier = modifier) {
        Text(
            text = pickupPoint.title,
            style = UiKitTheme.typography.secondary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = itemModifier,
        )

        Spacer(modifier = Modifier.height(16.dp))
        PickupPointInfoItem(
            title = stringResource(R.string.cart_working_hours),
            info = pickupPoint.schedule.capitalize(Locale.current),
            modifier = itemModifier,
        )

        Spacer(modifier = Modifier.height(12.dp))
        PickupPointInfoItem(
            title = stringResource(RCommon.string.res_address),
            info = pickupPoint.address,
            modifier = itemModifier,
        )

        if (pickupPoint.availablePaymentMethods.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            PickupPointPaymentMethodsItem(
                paymentMethods = pickupPoint.availablePaymentMethods,
                modifier = itemModifier,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        PickupPointInfoItem(
            title = stringResource(R.string.cart_shelf_time),
            info = pluralStringResource(
                id = R.plurals.cart_days,
                pickupPoint.shelfTimeInDays,
                pickupPoint.shelfTimeInDays,
            ),
            modifier = itemModifier,
        )

        Spacer(modifier = Modifier.height(12.dp))
        PickupPointInfoItem(
            title = stringResource(R.string.cart_tentative_delivery_date),
            info = pickupPoint.expectedDeliveryDate,
            modifier = itemModifier,
        )
    }
}

@Composable
private fun PickupPointInfoItem(
    title: String,
    info: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = UiKitTheme.typography.footnote.regular,
            color = UiKitTheme.colors.text.general.regular.muted,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = info,
            style = UiKitTheme.typography.secondary.light,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PickupPointPaymentMethodsItem(
    paymentMethods: Set<PickupPoint.PaymentMethod>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val paymentMethodStrings = remember(paymentMethods, context, configuration) {
        paymentMethods.map {
            when (it) {
                PickupPoint.PaymentMethod.CASH -> context.getString(R.string.cart_by_cash)
                PickupPoint.PaymentMethod.CARD -> context.getString(R.string.cart_by_card)
            }
        }
    }
    val paymentMethodsText = when (paymentMethods.size) {
        0 -> null
        1 -> paymentMethodStrings.first()
        else -> stringResource(
            R.string.cart_or,
            paymentMethodStrings[0],
            paymentMethodStrings[1],
        )
    }

    if (paymentMethodsText != null) {
        PickupPointInfoItem(
            title = stringResource(R.string.cart_payment),
            info = paymentMethodsText.lowercase().capitalize(Locale.current),
            modifier = modifier,
        )
    }
}

@Composable
private fun PickupPointDeliveryTerms(
    pickupPoint: PickupPointDetailed,
    selectedDeliveryTypeId: PickupPointDetailed.DeliveryType.Id,
    onDeliveryTypeClicked: (PickupPointDetailed.DeliveryType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.cart_choose_delivery_terms),
            style = UiKitTheme.typography.tertiary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        pickupPoint.deliveryTypes.forEachIndexed { index, deliveryType ->
            key(deliveryType.id.value) {
                ZarinaItem(
                    onClick = { onDeliveryTypeClicked(deliveryType) },
                    startContent = {
                        Column {
                            Text(
                                text = deliveryType.title,
                                style = UiKitTheme.typography.secondary.light,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = remember(deliveryType.description) {
                                    AnnotatedString.fromHtml(deliveryType.description)
                                },
                                style = UiKitTheme.typography.footnote.light,
                                color = UiKitTheme.colors.text.general.regular.muted,
                            )
                        }
                    },
                    endContent = {
                        ZarinaRadioButton(
                            isSelected = deliveryType.id == selectedDeliveryTypeId,
                            onClick = { onDeliveryTypeClicked(deliveryType) },
                        )
                    },
                    contentPadding = PaddingValues(start = 16.dp, top = 12.dp, bottom = 12.dp),
                )

                if (index < pickupPoint.deliveryTypes.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}
