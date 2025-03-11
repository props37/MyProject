package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uikit.item.ZarinaExpandableItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.impl.R

@Composable
internal fun ProductDeliveryAndPayment(
    freeDeliveryTotalPriceThreshold: Int,
    modifier: Modifier = Modifier,
) {
    val currentContext by rememberUpdatedState(LocalContext.current)

    ZarinaExpandableItem(
        header = {
            Text(text = stringResource(R.string.product_delivery_and_payment))
        },
        modifier = modifier,
    ) {
        val baseText = stringResource(
            id = R.string.product_delivery_and_payment_info,
            rememberFormattedPrice(freeDeliveryTotalPriceThreshold),
        )

        val clickableDeliveryText =
            stringResource(R.string.product_delivery_and_payment_info_delivery)
        val clickablePaymentText =
            stringResource(R.string.product_delivery_and_payment_info_payment)
        val clickableTextStyle = UiKitTheme.typography.tertiary.regular
        val deliveryAndPaymentUrl =
            stringResource(R.string.product_delivery_and_payment_info_delivery_payment_url)

        val text = remember(
            baseText,
            clickableDeliveryText,
            clickablePaymentText,
            clickableTextStyle,
            deliveryAndPaymentUrl,
        ) {
            buildAnnotatedString {
                append(baseText)

                val string = this.toAnnotatedString()
                val clickableDeliveryTextStartIndex = string.lastIndexOf(clickableDeliveryText)
                val clickablePaymentTextStartIndex = string.lastIndexOf(clickablePaymentText)

                val clickableSpanStyle = clickableTextStyle.toSpanStyle()
                val linkInteractionListener = { link: LinkAnnotation ->
                    if (link is LinkAnnotation.Url) {
                        currentContext.openUrlInCustomTabs(link.url)
                    }
                }
                if (clickableDeliveryTextStartIndex != -1) {
                    val end = clickableDeliveryTextStartIndex + clickableDeliveryText.length
                    addLink(
                        url = LinkAnnotation.Url(
                            url = deliveryAndPaymentUrl,
                            styles = TextLinkStyles(clickableSpanStyle),
                            linkInteractionListener = linkInteractionListener,
                        ),
                        start = clickableDeliveryTextStartIndex,
                        end = end,
                    )
                }
                if (clickablePaymentTextStartIndex != -1) {
                    val end = clickablePaymentTextStartIndex + clickablePaymentText.length
                    addLink(
                        url = LinkAnnotation.Url(
                            url = deliveryAndPaymentUrl,
                            styles = TextLinkStyles(clickableSpanStyle),
                            linkInteractionListener = linkInteractionListener,
                        ),
                        start = clickablePaymentTextStartIndex,
                        end = end,
                    )
                }
            }
        }

        Text(
            text = text,
            style = UiKitTheme.typography.tertiary.light,
            color = UiKitTheme.colors.text.general.regular.default,
        )
    }
}
