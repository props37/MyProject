package ru.livetyping.zarina.presentation.screen.checkout.courierdelivery.deliverydatetimeselector

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar

object CheckoutCourierDeliveryDateTimeSelectorScreenComponents {

    @Composable
    fun TopBar(
        selectorType: CourierDeliveryDateTimeSelectorType,
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                val textResId = when (selectorType) {
                    CourierDeliveryDateTimeSelectorType.DATE -> R.string.delivery_date
                    CourierDeliveryDateTimeSelectorType.TIME -> R.string.delivery_time
                }
                Text(
                    text = stringResource(textResId),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }
}
