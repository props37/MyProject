package ru.livetyping.zarina.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPrice
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CartPrice(
    cartPrice: Int,
    discountSize: Int,
    isDeliveryPriceIncluded: Boolean,
    deliveryPrice: Int?,
    giftCertificateWriteOffSize: Int?,
    finalPrice: Int,
    modifier: Modifier = Modifier,
    isFinalPriceDetailsButtonVisible: Boolean = false,
    onFinalPriceDetailsButtonClicked: (() -> Unit)? = null,
    backgroundColor: Color = BackgroundColor,
    contentColor: Color = ContentColor,
    contentPadding: PaddingValues = ContentPadding,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = modifier
                .background(backgroundColor)
                .padding(contentPadding),
        ) {
            PriceItem(
                name = stringResource(R.string.order_price),
                price = cartPrice,
                nameTextStyle = DefaultPriceNameTextStyle,
                priceTextStyle = DefaultPriceTextStyle,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            PriceItem(
                name = stringResource(R.string.discount),
                price = -discountSize,
                nameTextStyle = DefaultPriceNameTextStyle,
                priceTextStyle = DefaultPriceTextStyle,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            if (isDeliveryPriceIncluded && deliveryPrice != null) {
                PriceItem(
                    name = stringResource(R.string.delivery),
                    price = deliveryPrice,
                    nameTextStyle = DefaultPriceNameTextStyle,
                    priceTextStyle = DefaultPriceTextStyle,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }

            if (giftCertificateWriteOffSize != null) {
                PriceItem(
                    name = stringResource(R.string.written_off_from_gift_certificate),
                    price = giftCertificateWriteOffSize,
                    nameTextStyle = DefaultPriceNameTextStyle,
                    priceTextStyle = DefaultPriceTextStyle,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }

            PriceItem(
                name = stringResource(R.string.total),
                price = finalPrice,
                nameTextStyle = TotalPriceNameTextStyle,
                priceTextStyle = TotalPriceTextStyle,
                isDetailsButtonVisible = isFinalPriceDetailsButtonVisible,
                onDetailsButtonClicked = onFinalPriceDetailsButtonClicked,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            if (!isDeliveryPriceIncluded) {
                Text(
                    text = stringResource(R.string.excluding_delivery),
                    style = UiKitTheme.typography.tertiary.light,
                    color = UiKitTheme.colors.text.general.regular.muted,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PriceItem(
    name: String,
    price: Int,
    nameTextStyle: TextStyle,
    priceTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    isDetailsButtonVisible: Boolean = false,
    onDetailsButtonClicked: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(IntrinsicSize.Min),
    ) {
        Text(
            text = name,
            style = nameTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (isDetailsButtonVisible) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                val iconSize = 20.dp
                ZarinaIconButton(
                    onClick = { onDetailsButtonClicked?.invoke() },
                    indication = ripple(bounded = false, radius = iconSize),
                    modifier = Modifier
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .wrapContentSize(unbounded = true)
                        .size(iconSize),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_question_mark_shaped_24),
                        contentDescription = stringResource(R.string.show_details),
                        modifier = Modifier.size(iconSize),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))

        val formattedPrice = rememberFormattedPrice(price)
        Text(
            text = stringResource(R.string.price_in_rubles_string, formattedPrice),
            style = priceTextStyle,
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        CartPrice(
            cartPrice = 25739,
            discountSize = 4050,
            isDeliveryPriceIncluded = true,
            deliveryPrice = 550,
            giftCertificateWriteOffSize = null,
            finalPrice = 21743,
        )
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default

private val ContentPadding: PaddingValues get() = PaddingValues(16.dp)

private val DefaultPriceNameTextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.light

private val DefaultPriceTextStyle
    @Composable
    get() = UiKitTheme.typography.secondary.regular

private val TotalPriceNameTextStyle
    @Composable
    get() = UiKitTheme.typography.primary.regular

private val TotalPriceTextStyle
    @Composable
    get() = UiKitTheme.typography.primary.bold
