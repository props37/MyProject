package ru.livetyping.zarina.core.uikit.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.uicompose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.core.uikit.button.ZarinaAddToCartIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaLikeIconButton
import ru.livetyping.zarina.core.uikit.button.ZarinaSubscribeIconButton
import ru.livetyping.zarina.core.uikit.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.core.uikit.product.ProductCardDefaults.IconSize
import ru.livetyping.zarina.core.uikit.product.ProductCardDefaults.MediaAspectRatio
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun ProductCard(
    product: Product,
    onClick: (Product) -> Unit,
    onAddToFavoritesClicked: (Product) -> Unit,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
    backgroundColor: Color = ProductCardDefaults.BackgroundColor,
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick(product) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MediaAspectRatio),
        ) {
            val pagerState = rememberEndlessPagerState(itemCount = product.media.size)

            ZarinaMediaHorizontalPager(
                pagerState = pagerState,
                media = product.media,
                shimmer = shimmer,
                modifier = Modifier.matchParentSize(),
            )
            ZarinaLikeIconButton(
                isLiked = product.isInWishlist,
                onClick = { onAddToFavoritesClicked(product) },
                iconSize = IconSize,
                indication = ripple(bounded = false, radius = IconSize),
                modifier = Modifier.align(Alignment.TopEnd),
            )
            ZarinaHorizontalPagerIndicator(
                pagerState = pagerState,
                itemCount = product.media.size,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(
                text = product.name.uppercase(),
                style = UiKitTheme.typography.caption1.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 2.dp), // Circe font padding
            )
            Spacer(modifier = Modifier.width(8.dp))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                val buttonModifier = Modifier
                    .padding(end = 10.dp)
                    .size(28.dp)

                if (product.isAvailable) {
                    ZarinaAddToCartIconButton(
                        isAdded = product.isInCart,
                        onClick = { onAddToCartClicked(product) },
                        iconSize = IconSize,
                        modifier = buttonModifier,
                    )
                } else {
                    ZarinaSubscribeIconButton(
                        onClick = { onSubscribeClicked(product) },
                        iconSize = IconSize,
                        modifier = buttonModifier,
                    )
                }
            }
        }

        ProductPrice(
            price = product.price,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        ProductCardColors(
            colors = product.colors,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
public fun ProductCardSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    Column(modifier = modifier) {
        ZarinaSkeleton(
            shimmer = shimmer,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MediaAspectRatio),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(24.dp))
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(16.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(44.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(48.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption2.regular,
                shimmer = shimmer,
                modifier = Modifier.width(28.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.padding(start = 16.dp)) {
            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier.size(width = 42.dp, height = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

public object ProductCardDefaults {
    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    internal const val MediaAspectRatio = 0.68f

    internal val IconSize = 16.dp
}
