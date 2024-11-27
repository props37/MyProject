package ru.livetyping.zarina.core.uikit.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.domain.model.order.OrderShort
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uicompose.rememberFormattedLocalDate
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.label.ZarinaLabelSize
import ru.livetyping.zarina.core.uikit.order.OrderCardDefaults.DateFormatterPattern
import ru.livetyping.zarina.core.uikit.order.OrderCardDefaults.ProductImageSize
import ru.livetyping.zarina.core.uikit.order.OrderCardDefaults.ProductImageSpaceBetween
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import java.time.LocalDate
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun OrderCard(
    order: OrderShort,
    onClick: ((OrderShort) -> Unit)?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = OrderCardDefaults.BackgroundColor,
    contentPadding: PaddingValues = OrderCardDefaults.ContentPadding,
) {
    val orderProductImageUrls = remember {
        order.products.map { it.imageUrl.value }
    }

    OrderCard(
        orderNumber = order.number.value,
        orderStatusName = stringResource(order.status.nameResId),
        orderStatusColor = order.status.color,
        orderTotalPrice = order.totalPrice,
        orderDate = order.date,
        orderProductCount = order.productCount,
        orderProductImageUrls = orderProductImageUrls,
        onClick = if (onClick != null) {
            { onClick(order) }
        } else {
            null
        },
        backgroundColor = backgroundColor,
        contentPadding = contentPadding,
        modifier = modifier,
    )
}

@Composable
public fun OrderCard(
    orderNumber: String,
    orderStatusName: String,
    orderStatusColor: Color,
    orderTotalPrice: Int,
    orderDate: LocalDate,
    orderProductCount: Int,
    orderProductImageUrls: List<String>,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = OrderCardDefaults.BackgroundColor,
    contentPadding: PaddingValues = OrderCardDefaults.ContentPadding,
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() },
            )
            .padding(contentPadding),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = UiKitTheme.typography.secondary.regular
            val color = UiKitTheme.colors.text.general.regular.default

            Text(
                text = "№ $orderNumber",
                style = textStyle,
                color = color,
            )

            Spacer(modifier = Modifier.width(10.dp))
            OrderStatusLabel(
                statusName = orderStatusName,
                statusColor = orderStatusColor,
                size = ZarinaLabelSize.Small,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(12.dp))

            val formattedPrice = rememberFormattedPrice(orderTotalPrice)
            Text(
                text = stringResource(RCommon.string.res_price_in_rubles, formattedPrice),
                style = textStyle,
                color = color,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = UiKitTheme.typography.tertiary.light
            val color = UiKitTheme.colors.text.general.regular.muted

            val formattedDate = rememberFormattedLocalDate(
                localDate = orderDate,
                formatterPattern = DateFormatterPattern,
            )
            Text(
                text = stringResource(R.string.uikit_order_from, formattedDate),
                style = textStyle,
                color = color,
            )

            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = pluralStringResource(
                    id = RCommon.plurals.product_count,
                    count = orderProductCount,
                    orderProductCount.toString(),
                ),
                style = textStyle,
                color = color,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Products(
            productImageUrls = orderProductImageUrls,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
public fun OrderCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window),
    contentPadding: PaddingValues = OrderCardDefaults.ContentPadding,
) {
    Column(modifier = modifier.padding(contentPadding)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.secondary.regular,
                shimmer = shimmer,
                modifier = Modifier.width(72.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption2.bold,
                shimmer = shimmer,
                modifier = Modifier.width(48.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.secondary.regular,
                shimmer = shimmer,
                modifier = Modifier.width(56.dp),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.light,
                shimmer = shimmer,
                modifier = Modifier.width(128.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.light,
                shimmer = shimmer,
                modifier = Modifier.width(72.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ProductImageSpaceBetween),
        ) {
            repeat(times = 6) {
                ZarinaSkeleton(
                    shimmer = shimmer,
                    shape = RectangleShape,
                    modifier = Modifier.size(ProductImageSize),
                )
            }
        }
    }
}

@Composable
private fun Products(
    productImageUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer()

    BoxWithConstraints(modifier = modifier) {
        val maxImageCount = (maxWidth / (ProductImageSize.width + ProductImageSpaceBetween)).toInt()
        Row(
            horizontalArrangement = Arrangement.spacedBy(ProductImageSpaceBetween),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(minOf(productImageUrls.size, maxImageCount)) { i ->
                val imageUrl = productImageUrls[i]
                var isImageShimmerEnabled by remember(imageUrl) { mutableStateOf(true) }
                key(imageUrl) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { isImageShimmerEnabled = false },
                        modifier = Modifier
                            .size(ProductImageSize)
                            .shimmerToggleable(
                                shimmer = shimmer,
                                isEnabled = isImageShimmerEnabled,
                            )
                            .background(UiKitTheme.colors.background.skeleton),
                    )
                }
            }
        }
    }
}

public object OrderCardDefaults {
    public val ContentPadding: PaddingValues
        get() = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp)

    internal val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    internal val ProductImageSize: DpSize get() = DpSize(40.dp, 56.dp)
    internal val ProductImageSpaceBetween: Dp get() = 4.dp

    internal const val DateFormatterPattern = "dd MMMM yyyy"
}
