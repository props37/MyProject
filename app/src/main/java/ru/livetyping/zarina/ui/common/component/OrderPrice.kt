package ru.livetyping.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun OrderPrice(
    orderPrice: Long,
    deliveryPrice: Long,
    totalPrice: Long,
    modifier: Modifier = Modifier,
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
                price = orderPrice,
                nameTextStyle = DefaultPriceNameTextStyle,
                priceTextStyle = DefaultPriceTextStyle,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            PriceItem(
                name = stringResource(R.string.delivery),
                price = deliveryPrice,
                nameTextStyle = DefaultPriceNameTextStyle,
                priceTextStyle = DefaultPriceTextStyle,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            PriceItem(
                name = stringResource(R.string.total),
                price = totalPrice,
                nameTextStyle = TotalPriceNameTextStyle,
                priceTextStyle = TotalPriceTextStyle,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
fun OrderPriceSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    contentPadding: PaddingValues = ContentPadding,
) {
    Column(modifier = modifier.padding(contentPadding)) {
        PriceItemSkeleton(
            nameWidth = 64.dp,
            priceWidth = 36.dp,
            nameTextStyle = DefaultPriceNameTextStyle,
            priceTextStyle = DefaultPriceTextStyle,
            shimmer = shimmer,
            modifier = Modifier.padding(vertical = 4.dp),
        )

        PriceItemSkeleton(
            nameWidth = 64.dp,
            priceWidth = 36.dp,
            nameTextStyle = DefaultPriceNameTextStyle,
            priceTextStyle = DefaultPriceTextStyle,
            shimmer = shimmer,
            modifier = Modifier.padding(vertical = 4.dp),
        )

        PriceItemSkeleton(
            nameWidth = 72.dp,
            priceWidth = 44.dp,
            nameTextStyle = TotalPriceNameTextStyle,
            priceTextStyle = TotalPriceTextStyle,
            shimmer = shimmer,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun PriceItem(
    name: String,
    price: Long,
    nameTextStyle: TextStyle,
    priceTextStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = name,
            style = nameTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(16.dp))

        val formattedPrice = rememberFormattedPrice(price)
        Text(
            text = stringResource(R.string.price_in_rubles_string, formattedPrice),
            style = priceTextStyle,
        )
    }
}

@Composable
private fun PriceItemSkeleton(
    nameWidth: Dp,
    priceWidth: Dp,
    nameTextStyle: TextStyle,
    priceTextStyle: TextStyle,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        ZarinaTextSkeleton(
            textStyle = nameTextStyle,
            shimmer = shimmer,
            modifier = Modifier.width(nameWidth),
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))

        ZarinaTextSkeleton(
            textStyle = priceTextStyle,
            shimmer = shimmer,
            modifier = Modifier.width(priceWidth),
        )
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        OrderPrice(
            orderPrice = 15094,
            deliveryPrice = 99,
            totalPrice = 15193,
        )
    }
}

@Preview
@Composable
private fun PreviewSkeleton() {
    ZarinaPreview {
        OrderPriceSkeleton(
            modifier = Modifier.background(Color.White),
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
