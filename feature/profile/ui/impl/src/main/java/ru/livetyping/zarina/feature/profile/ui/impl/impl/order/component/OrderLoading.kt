package ru.livetyping.zarina.feature.profile.ui.impl.impl.order.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.order.OrderPriceSkeleton
import ru.livetyping.zarina.core.uikit.product.ProductOrderCardSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun OrderLoading(modifier: Modifier = Modifier) {
    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp),
        modifier = modifier,
    ) {
        item {
            ZarinaItem(modifier = Modifier.heightIn(min = 40.dp)) {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.footnote.bold,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.08f),
                )
            }
        }

        item {
            ZarinaItem(
                startContent = {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.bold,
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(fraction = 0.2f),
                    )
                },
                endContent = {
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.light,
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(fraction = 0.16f),
                    )
                },
                modifier = Modifier.heightIn(min = 40.dp),
            )
        }

        items(OrderSkeletonProductCount) { index ->
            Column {
                ProductOrderCardSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < OrderSkeletonProductCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }

        item {
            OrderPriceSkeleton(
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            OrderInfoSkeleton(shimmer)
        }

        item {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
            )
        }
    }
}

@Composable
private fun OrderInfoSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = OrderInfoContentPadding,
) {
    Column(modifier = modifier.padding(contentPadding)) {
        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.secondary.bold,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(fraction = 0.2f),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OrderInfoItemSkeleton(shimmer = shimmer)
        Spacer(modifier = Modifier.height(12.dp))
        OrderInfoItemSkeleton(shimmer = shimmer)
        Spacer(modifier = Modifier.height(12.dp))
        OrderInfoItemSkeleton(shimmer = shimmer)
        Spacer(modifier = Modifier.height(12.dp))
        OrderInfoItemSkeleton(shimmer = shimmer)
    }
}

@Composable
private fun OrderInfoItemSkeleton(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ZarinaTextSkeleton(
            textStyle = OrderInfoNameTextStyle,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(fraction = 0.25f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        ZarinaTextSkeleton(
            textStyle = OrderInfoValueTextStyle,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(fraction = 0.42f),
        )
    }
}

private const val OrderSkeletonProductCount = 5
