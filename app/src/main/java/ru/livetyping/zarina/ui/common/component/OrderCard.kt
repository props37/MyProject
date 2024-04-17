package ru.livetyping.zarina.ui.common.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.common.util.rememberFormattedLocalDate
import ru.livetyping.zarina.ui.common.util.rememberFormattedPrice
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

@Composable
fun OrderCard(
    order: OrderItem,
    onClick: (OrderItem) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundColor,
    contentPadding: PaddingValues = ContentPadding,
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick(order) }
            .padding(contentPadding),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = UiKitTheme.typography.secondary.regular
            val color = UiKitTheme.colors.text.general.regular.default

            Text(
                text = stringResource(R.string.number_symbol),
                style = textStyle,
                color = color,
            )
            Spacer(modifier = Modifier.width(8.dp))
            // TODO: [High] Add status

            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.weight(1f))

            val formattedPrice = rememberFormattedPrice(order.totalPrice)
            Text(
                text = stringResource(R.string.price_in_rubles_string, formattedPrice),
                style = textStyle,
                color = color,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val textStyle = UiKitTheme.typography.tertiary.light
            val color = UiKitTheme.colors.text.general.regular.muted

            val formattedDate = rememberFormattedLocalDate(
                localDate = order.date,
                formatterPattern = DateFormatterPattern,
            )
            Text(
                text = stringResource(R.string.order_from, formattedDate),
                style = textStyle,
                color = color,
            )

            Spacer(modifier = Modifier.width(8.dp))
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = pluralStringResource(R.plurals.products, order.productCount),
                style = textStyle,
                color = color,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Products(
            products = order.products,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun OrderCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window),
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 72.dp, height = 16.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 48.dp, height = 12.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 56.dp, height = 16.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 128.dp, height = 16.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 72.dp, height = 16.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
    products: List<OrderItem.Product>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxImageCount = (maxWidth / (ProductImageSize.width + ProductImageSpaceBetween)).toInt()
        Row(
            horizontalArrangement = Arrangement.spacedBy(ProductImageSpaceBetween),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(minOf(products.size, maxImageCount)) { i ->
                val product = products[i]
                val imageUrl = product.imageUrl
                var isImageShimmerEnabled by remember(imageUrl) { mutableStateOf(true) }
                key(imageUrl.value) {
                    AsyncImage(
                        model = product.imageUrl.value,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { isImageShimmerEnabled = false },
                        modifier = Modifier
                            .size(ProductImageSize)
                            .shimmerToggleable(
                                shimmer = rememberZarinaSkeletonShimmer(),
                                isEnabled = isImageShimmerEnabled,
                            )
                            .background(UiKitTheme.colors.background.skeleton),
                    )
                }
            }
        }
    }
}

@Preview
@DensityPreviews
@Composable
private fun PreviewSkeleton() {
    ZarinaPreview {
        OrderCardSkeleton(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentPadding: PaddingValues
    get() = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp)

private const val DateFormatterPattern = "dd MMMM yyyy"

private val ProductImageSize: DpSize get() = DpSize(40.dp, 56.dp)
private val ProductImageSpaceBetween: Dp get() = 4.dp
