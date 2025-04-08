package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector.DeliveryOptionDateTimeSelectorType
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    selectorType: DeliveryOptionDateTimeSelectorType,
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
                DeliveryOptionDateTimeSelectorType.DATE -> RCommon.string.res_delivery_date
                DeliveryOptionDateTimeSelectorType.TIME -> RCommon.string.res_delivery_time
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
