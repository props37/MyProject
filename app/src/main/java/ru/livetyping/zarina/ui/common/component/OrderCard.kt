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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.livetyping.zarina.domain.order.OrderItem
import ru.livetyping.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
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
            // TODO: [High] Implement
        }
 
        Row(verticalAlignment = Alignment.CenterVertically) {
            // TODO: [High] Implement
        }

        Spacer(modifier = Modifier.height(8.dp))

        Products(
            products = order.products,
            modifier = Modifier.fillMaxWidth(),
        )
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

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentPadding: PaddingValues
    get() = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp)

private val ProductImageSize: DpSize get() = DpSize(40.dp, 56.dp)
private val ProductImageSpaceBetween: Dp get() = 4.dp
