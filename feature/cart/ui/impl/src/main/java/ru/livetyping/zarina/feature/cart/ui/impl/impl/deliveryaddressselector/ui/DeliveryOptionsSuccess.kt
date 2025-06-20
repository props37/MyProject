package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryOptionsState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun DeliveryOptionsSuccess(
    state: DeliveryOptionsState.Success,
    onDeliveryOptionClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionDateClicked: ((DeliveryOption) -> Unit)?,
    onDeliveryOptionTimeClicked: ((DeliveryOption) -> Unit)?,
    onDeliveryOptionShowDetailsClicked: (DeliveryOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        state.options.forEach { optionState ->
            key(optionState.deliveryOption.id.value) {
                val option = optionState.deliveryOption
                DeliveryOption(
                    title = stringResource(RCommon.string.res_delivery_method_courier),
                    price = option.price,
                    deliveryDate = optionState.selectedDateTimePeriod.date,
                    deliveryTime = optionState.selectedDateTimePeriod.time,
                    isSelected = optionState.isSelected,
                    onClick = { onDeliveryOptionClicked(option) },
                    onDeliveryDateClicked = onDeliveryOptionDateClicked?.let { { it(option) } },
                    onDeliveryTimeClicked = onDeliveryOptionTimeClicked?.let { { it(option) } },
                    onShowDetailsClicked = { onDeliveryOptionShowDetailsClicked(option) },
                )
            }
        }
    }
}
