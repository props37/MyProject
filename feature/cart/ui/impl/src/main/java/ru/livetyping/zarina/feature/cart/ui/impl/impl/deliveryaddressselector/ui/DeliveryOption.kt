package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.cart.ui.impl.R
import java.math.BigDecimal
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun DeliveryOption(
    title: String,
    price: BigDecimal,
    deliveryDate: String,
    deliveryTime: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDeliveryDateClicked: (() -> Unit)?,
    onDeliveryTimeClicked: (() -> Unit)?,
    onShowDetailsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            UiKitTheme2.colors.mainBlack
        } else {
            UiKitTheme2.colors.gray
        },
        label = "border color",
    )

    Column(
        modifier = modifier
            .clip(DeliveryOptionShape)
            .background(
                color = UiKitTheme2.colors.white,
                shape = DeliveryOptionShape,
            )
            .border(width = 0.5.dp, color = borderColor, shape = DeliveryOptionShape),
    ) {
        ZarinaItem(
            onClick = onClick,
            startContent = {
                Text(
                    text = title.uppercase(),
                    style = UiKitTheme2.typography.body,
                )
            },
            endContent = {
                val formattedPrice = rememberFormattedPrice(price)
                Text(
                    text = stringResource(RCommon.string.res_price_in_rubles, formattedPrice).uppercase(),
                    style = UiKitTheme2.typography.body,
                    color = UiKitTheme2.colors.middleGray,
                    maxLines = 1,
                )

                val iconSize = 16.dp
                ZarinaIconButton(
                    onClick = onShowDetailsClicked,
                    indication = ripple(bounded = false, radius = iconSize),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_exclamation_mark_inscribed_in_circle_24),
                        contentDescription = stringResource(RCommon.string.res_show_details),
                        modifier = Modifier.size(iconSize),
                    )
                }
            },
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
        )

        AnimatedVisibility(visible = isSelected) {
            Column {
                DeliveryOptionDateTime(
                    label = stringResource(RCommon.string.res_delivery_date),
                    text = deliveryDate,
                    onClick = onDeliveryDateClicked,
                )

                if (deliveryTime != null) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )

                    DeliveryOptionDateTime(
                        label = stringResource(RCommon.string.res_delivery_time),
                        text = deliveryTime,
                        onClick = onDeliveryTimeClicked,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DeliveryOptionDateTime(
    label: String,
    text: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
) {
    ZarinaItem(
        onClick = onClick,
        startContent = {
            Column {
                Text(
                    text = label.uppercase(),
                    style = UiKitTheme2.typography.body,
                    color = UiKitTheme2.colors.middleGray,
                    maxLines = 1,
                )
                Text(
                    text = text.uppercase(),
                    style = UiKitTheme2.typography.body,
                    maxLines = 1,
                )
            }
        },
        endContent = {
            if (onClick != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                    contentDescription = stringResource(R.string.cart_change_delivery_date),
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(degrees = 90f),
                )
            }
        },
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

private val DeliveryOptionShape: Shape
    get() = RoundedCornerShape(2.dp)
