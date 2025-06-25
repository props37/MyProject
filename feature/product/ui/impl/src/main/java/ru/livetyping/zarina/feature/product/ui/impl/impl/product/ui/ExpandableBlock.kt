package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.item.ZarinaExpandableItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ProductDetailsBlock(
    product: ProductDetailed,
    modifier: Modifier = Modifier,
) {
    ZarinaExpandableItem(
        header = {
            Header(text = stringResource(R.string.product_details).uppercase())
        },
        backgroundColor = ExpandableBlockBackgroundColor,
        headerContentPadding = PaddingValues(horizontal = 16.dp),
        contentPadding = BodyContentPadding,
        modifier = modifier,
    ) {
        val text = rememberProductDetailsText(product)

        Text(
            text = text.toUpperCase(),
            style = BodyTextStyle,
            color = UiKitTheme2.colors.mainBlack,
        )
    }
}

@Composable
internal fun DeliveryAndPaymentBlock(
    freeDeliveryThreshold: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    ZarinaExpandableItem(
        header = {
            Header(text = stringResource(R.string.product_delivery_and_payment).uppercase())
        },
        backgroundColor = ExpandableBlockBackgroundColor,
        headerContentPadding = PaddingValues(horizontal = 16.dp),
        contentPadding = BodyContentPadding,
        modifier = modifier,
    ) {
        val url = stringResource(RCommon.string.res_zarina_delivery_and_payment_url)
        val delivery = stringResource(R.string.product_delivery_and_payment_info_delivery)
        val payment = stringResource(R.string.product_delivery_and_payment_info_payment)
        val text = rememberAnnotatedStringWithLinks(
            baseString = stringResource(
                id = R.string.product_delivery_and_payment_info,
                freeDeliveryThreshold,
            ),
            substringToUrl = remember { mapOf(delivery to url, payment to url) },
            linkStyle = BodyTextStyle.copy(textDecoration = TextDecoration.Underline).toSpanStyle(),
            onUrlClicked = context::openUrlInCustomTabs,
        )

        Text(
            text = text.toUpperCase(),
            style = BodyTextStyle,
            color = UiKitTheme2.colors.mainBlack,
        )
    }
}

@Composable
private fun Header(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .heightIn(min = ExpandableBlockHeaderHeight)
            .padding(vertical = 8.dp),
    ) {
        Text(text = text, style = ExpandableBlockHeaderTextStyle)
    }
}

@Composable
private fun rememberProductDetailsText(product: ProductDetailed): AnnotatedString {
    val context = LocalContext.current
    // Subscribe to Configuration changes
    val configuration = LocalConfiguration.current

    val keyStyle = BodyTextStyle.copy(color = UiKitTheme2.colors.middleGray).toSpanStyle()

    return remember(product, keyStyle, context, configuration) {
        buildAnnotatedString {
            product.description.forEach { entry ->
                appendKeyValue(
                    key = entry.title,
                    value = entry.body,
                    keyStyle = keyStyle,
                )
                appendTwoLines()
            }

            product.modelInfo?.sizeOnModel?.let {
                appendKeyValue(
                    key = context.getString(R.string.product_size_on_model),
                    value = it,
                    keyStyle = keyStyle,
                )
                appendTwoLines()
            }

            // TODO: [Top] Implement
//            product.modelInfo?.modelParams?.let {
//                appendKeyValue(
//                    key = context.getString(R.string.product_model_parameters),
//                    value = it,
//                    keyStyle = keyStyle,
//                )
//            }
        }
    }
}

private fun AnnotatedString.Builder.appendKeyValue(
    key: String,
    value: String,
    keyStyle: SpanStyle,
) {
    withStyle(keyStyle) {
        append(key)
        append(TextKeyValueSeparator)
    }
    append(value)
}

@Suppress("NOTHING_TO_INLINE")
private inline fun AnnotatedString.Builder.appendTwoLines() {
    appendLine()
    appendLine()
}

internal val ExpandableBlockBackgroundColor: Color
    @Composable
    get() = UiKitTheme2.colors.lightGray

internal val ExpandableBlockHeaderTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body

internal val ExpandableBlockHeaderHeight: Dp get() = 64.dp

private val BodyTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body

private val BodyContentPadding: PaddingValues
    get() = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 24.dp)

private const val TextKeyValueSeparator = ": "
