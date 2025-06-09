package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.item.ZarinaExpandableItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

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
        backgroundColor = BackgroundColor,
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
        modifier = modifier.heightIn(min = 64.dp),
    ) {
        Text(text = text, style = HeaderTextStyle)
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme2.colors.lightGray

private val HeaderTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.h4

private val BodyTextStyle: TextStyle
    @Composable
    get() = UiKitTheme2.typography.body

private val BodyContentPadding: PaddingValues
    get() = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 24.dp)
