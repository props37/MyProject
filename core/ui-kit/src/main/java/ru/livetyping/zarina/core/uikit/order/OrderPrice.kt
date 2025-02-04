package ru.livetyping.zarina.core.uikit.order

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uikit.order.OrderPriceDefaults.ContentPadding
import ru.livetyping.zarina.core.uikit.order.OrderPriceDefaults.DefaultPriceNameTextStyle
import ru.livetyping.zarina.core.uikit.order.OrderPriceDefaults.DefaultPriceTextStyle
import ru.livetyping.zarina.core.uikit.order.OrderPriceDefaults.TotalPriceNameTextStyle
import ru.livetyping.zarina.core.uikit.order.OrderPriceDefaults.TotalPriceTextStyle
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun OrderPrice(
    orderPrice: Int,
    deliveryPrice: Int,
    totalPrice: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = OrderPriceDefaults.BackgroundColor,
    contentColor: Color = OrderPriceDefaults.ContentColor,
    contentPadding: PaddingValues = OrderPriceDefaults.ContentPadding,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = modifier
                .background(backgroundColor)
                .padding(contentPadding),
        ) {
            PriceItem(
                name = stringResource(RCommon.string.res_order_price),
                price = orderPrice,
                nameTextStyle = DefaultPriceNameTextStyle,
                priceTextStyle = DefaultPriceTextStyle,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            PriceItem(
                name = stringResource(RCommon.string.res_delivery),
                price = deliveryPrice,
                nameTextStyle = DefaultPriceNameTextStyle,
                priceTextStyle = DefaultPriceTextStyle,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            PriceItem(
                name = stringResource(RCommon.string.res_total),
                price = totalPrice,
                nameTextStyle = TotalPriceNameTextStyle,
                priceTextStyle = TotalPriceTextStyle,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
public fun OrderPriceSkeleton(
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
    price: Int,
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
            text = stringResource(RCommon.string.res_price_in_rubles, formattedPrice),
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

internal object OrderPriceDefaults {
    val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    val ContentPadding: PaddingValues
        get() = PaddingValues(16.dp)

    val DefaultPriceNameTextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    val DefaultPriceTextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.regular

    val TotalPriceNameTextStyle
        @Composable
        get() = UiKitTheme.typography.primary.regular

    val TotalPriceTextStyle
        @Composable
        get() = UiKitTheme.typography.primary.bold
}
