package ru.livetyping.zarina.presentation.common.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaLikeIconButton
import ru.livetyping.zarina.presentation.common.component.color.ZarinaColorIcon
import ru.livetyping.zarina.presentation.common.component.media.ZarinaMediaHorizontalPager
import ru.livetyping.zarina.presentation.common.component.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.common.tooling.preview.parameterprovider.ProductPreviewParameterProvider
import ru.livetyping.zarina.presentation.common.util.domain.toComposeColor
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductCard(
    product: Product,
    onClick: (Product) -> Unit,
    onAddToFavoritesClicked: (Product) -> Unit,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberZarinaSkeletonShimmer(),
    backgroundColor: Color = BackgroundColor,
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
                isLiked = product.isInFavorites,
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
                    AddToCartIconButton(
                        isAdded = product.isInCart,
                        onClick = { onAddToCartClicked(product) },
                        modifier = buttonModifier,
                    )
                } else {
                    SubscribeIconButton(
                        onClick = { onSubscribeClicked(product) },
                        modifier = buttonModifier,
                    )
                }
            }
        }

        ProductPrice(
            price = product.price,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Colors(
            colors = product.colors,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProductCardSmall(
    product: Product,
    onClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberZarinaSkeletonShimmer(),
    backgroundColor: Color = BackgroundColor,
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick(product) },
    ) {
        var isImageDisplayed by remember(product.media) { mutableStateOf(false) }
        AsyncImage(
            model = remember(product.media) { product.media.first().originalUrl.value },
            contentDescription = null,
            onSuccess = { isImageDisplayed = true },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MediaAspectRatio)
                .shimmerToggleable(shimmer, isEnabled = !isImageDisplayed)
                .background(UiKitTheme.colors.background.skeleton),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name.uppercase(),
            style = UiKitTheme.typography.caption1.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        ProductPrice(price = product.price)

        Colors(colors = product.colors)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProductCardSkeleton(
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

@Composable
fun ProductCardSmallSkeleton(
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

        ZarinaTextSkeleton(
            textStyle = UiKitTheme.typography.caption1.regular,
            shimmer = shimmer,
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(32.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption1.regular,
                shimmer = shimmer,
                modifier = Modifier.width(32.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.caption2.regular,
                shimmer = shimmer,
                modifier = Modifier.width(20.dp),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ZarinaSkeleton(
            shimmer = shimmer,
            modifier = Modifier.size(width = 42.dp, height = 8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AddToCartIconButton(
    isAdded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    ZarinaIconButton(
        onClick = onClick,
        isLoading = isLoading,
        indication = ripple(bounded = false, radius = IconSize),
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isAdded,
            label = "AddToCartIconButton",
        ) { isAdded ->
            val iconResId =
                if (isAdded) R.drawable.ic_shopper_checkmark_outline_24 else R.drawable.ic_shopper_outline_24
            val contentDescriptionResId =
                if (isAdded) R.string.remove_from_cart else R.string.add_to_cart

            Icon(
                painter = painterResource(iconResId),
                contentDescription = stringResource(contentDescriptionResId),
                tint = UiKitTheme.colors.icon.regular.default,
                modifier = Modifier.size(IconSize),
            )
        }
    }
}

@Composable
private fun SubscribeIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    ZarinaIconButton(
        onClick = onClick,
        isLoading = isLoading,
        indication = ripple(bounded = false, radius = IconSize),
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bell_24),
            contentDescription = stringResource(R.string.subscribe_to_product),
            tint = UiKitTheme.colors.icon.regular.default,
            modifier = Modifier.size(IconSize),
        )
    }
}

@Composable
private fun Colors(
    colors: List<ProductColor>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidth = maxWidth
        Row(
            horizontalArrangement = Arrangement.spacedBy(ColorSpacedBy),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val colorsFit = remember(maxWidth, colors.size) {
                val colorsAvailableWidth = maxWidth - ColorsMoreTextWidth
                (colorsAvailableWidth / (ColorSize + ColorSpacedBy))
                    .toInt()
                    .coerceIn(0, colors.size)
            }
            val colorsLeft = remember(colorsFit, colors.size) {
                (colors.size - colorsFit).coerceIn(0, colors.size)
            }

            for (i in 0 until colorsFit) {
                val color = colors.getOrNull(i)
                if (color != null) {
                    key(color.id.value) {
                        ZarinaColorIcon(color = color.color.toComposeColor() ?: Color.Unspecified)
                    }
                }
            }

            val moreColorsText = if (colorsLeft > 0) "+$colorsLeft" else ""
            Text(
                text = moreColorsText,
                style = UiKitTheme.typography.caption2.regular,
                color = UiKitTheme.colors.text.general.regular.muted,
            )
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewProductCard(
    @PreviewParameter(ProductPreviewParameterProvider::class, 1)
    product: Product,
) {
    ZarinaPreview {
        ProductCard(
            product = product,
            onClick = {},
            onAddToFavoritesClicked = {},
            onAddToCartClicked = {},
            onSubscribeClicked = {},
            modifier = Modifier.background(Color.White),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun PreviewProductCardSmall(
    @PreviewParameter(ProductPreviewParameterProvider::class, 1)
    product: Product,
) {
    ZarinaPreview {
        ProductCardSmall(
            product = product,
            onClick = {},
            modifier = Modifier.background(Color.White),
        )
    }
}

@Preview
@Composable
private fun PreviewProductCardSkeleton() {
    ZarinaPreview {
        ProductCardSkeleton(modifier = Modifier.background(Color.White))
    }
}

@Preview
@Composable
private fun PreviewProductCardSmallSkeleton() {
    ZarinaPreview {
        ProductCardSmallSkeleton(modifier = Modifier.background(Color.White))
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private const val MediaAspectRatio = 0.68f

private val IconSize: Dp get() = 16.dp

private val ColorSize: Dp get() = 8.dp
private val ColorSpacedBy: Dp get() = ColorSize
private val ColorsMoreTextWidth = 16.dp
