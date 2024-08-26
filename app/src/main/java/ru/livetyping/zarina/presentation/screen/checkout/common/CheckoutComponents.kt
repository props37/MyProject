package ru.livetyping.zarina.presentation.screen.checkout.common

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPrice
import ru.livetyping.zarina.presentation.screen.checkout.common.CheckoutComponents.DeliveryOption
import ru.livetyping.zarina.presentation.theme.UiKitTheme

object CheckoutComponents {

    @Composable
    fun TopBar(
        title: String,
        step: Int,
        stepCount: Int,
        isBackButtonVisible: Boolean,
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
        onBackClicked: (() -> Unit)? = null,
    ) {
        ZarinaTopBar(
            startContent = {
                if (isBackButtonVisible) {
                    ZarinaBackIconButton(
                        onClick = { onBackClicked?.invoke() },
                        iconSize = 20.dp,
                        modifier = Modifier.padding(start = 2.dp),
                    )
                }
            },
            centerContent = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = title)
                    Text(
                        text = stringResource(R.string.step_number, step, stepCount),
                        style = UiKitTheme.typography.tertiary.light,
                    )
                }
            },
            endContent = {
                ZarinaCloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun DeliveryOption(
        title: String,
        price: Int,
        deliveryDate: String,
        deliveryTime: String?,
        isSelected: Boolean,
        onClick: () -> Unit,
        onDeliveryDateClicked: (() -> Unit)?,
        onDeliveryTimeClicked: (() -> Unit)?,
        onShowDetailsClicked: () -> Unit,
        modifier: Modifier = Modifier,
        backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
    ) {
        val borderColor by animateColorAsState(
            targetValue = if (isSelected) {
                UiKitTheme.colors.border.general.active
            } else {
                UiKitTheme.colors.border.general.default
            },
            label = "border color",
        )

        Column(
            modifier = modifier
                .clip(DeliveryOptionShape)
                .background(color = backgroundColor, shape = DeliveryOptionShape)
                .border(width = 0.5.dp, color = borderColor, shape = DeliveryOptionShape),
        ) {
            ZarinaItem(
                onClick = onClick,
                startContent = {
                    Text(
                        text = title,
                        style = UiKitTheme.typography.secondary.regular,
                    )
                },
                endContent = {
                    val formattedPrice = rememberFormattedPrice(price)
                    Text(
                        text = stringResource(R.string.price_in_rubles_string, formattedPrice),
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                        maxLines = 1,
                    )

                    val iconSize = 16.dp
                    ZarinaIconButton(
                        onClick = onShowDetailsClicked,
                        indication = ripple(bounded = false, radius = iconSize),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_exclamation_mark_shaped_24),
                            contentDescription = stringResource(R.string.show_details),
                            modifier = Modifier.size(iconSize),
                        )
                    }
                },
                contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
            )

            AnimatedVisibility(visible = isSelected) {
                Column {
                    DeliveryOptionDateTime(
                        label = stringResource(R.string.delivery_date),
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
                            label = stringResource(R.string.delivery_time),
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
                        text = label,
                        style = UiKitTheme.typography.footnote.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                        maxLines = 1,
                    )
                    Text(
                        text = text,
                        style = UiKitTheme.typography.secondary.light,
                        maxLines = 1,
                    )
                }
            },
            endContent = {
                if (onClick != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                        contentDescription = stringResource(R.string.change_delivery_date),
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

    private val DeliveryOptionShape: Shape get() = RoundedCornerShape(2.dp)
}

@Preview
@Composable
private fun DeliveryOptionPreview() {
    ZarinaPreview {
        var isSelected by remember { mutableStateOf(true) }

        DeliveryOption(
            title = "С примеркой",
            price = 550,
            deliveryDate = "03 января, среда",
            deliveryTime = "18:00 - 20:00",
            isSelected = isSelected,
            onClick = { isSelected = !isSelected },
            onDeliveryDateClicked = {},
            onDeliveryTimeClicked = null,
            onShowDetailsClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
