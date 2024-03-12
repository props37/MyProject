package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductColor
import ru.zarina.zarina.domain.product.currentPrice
import ru.zarina.zarina.ui.common.component.button.ZarinaLikeIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaIconButton
import ru.zarina.zarina.ui.common.component.pager.HorizontalPagerIndicator
import ru.zarina.zarina.ui.common.component.skeleton.Skeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.common.tooling.preview.parameterprovider.ProductPreviewParameterProvider
import ru.zarina.zarina.ui.common.util.domain.toComposeColor
import ru.zarina.zarina.ui.common.util.rememberFormattedPrice
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.rememberEndlessPagerState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToFavoritesClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    onSubscribeClicked: () -> Unit,
    modifier: Modifier = Modifier,
    shimmer: Shimmer? = rememberSkeletonShimmer(),
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MediaAspectRatio),
        ) {
            val pagerState = rememberEndlessPagerState(itemCount = product.media.size)

            MediaHorizontalPager(
                pagerState = pagerState,
                medias = product.media,
                shimmer = shimmer,
                modifier = Modifier.matchParentSize(),
            )
            ZarinaLikeIconButton(
                isLiked = product.isInFavorites,
                onClick = onAddToFavoritesClicked,
                iconSize = IconSize,
                indication = rememberRipple(bounded = false, radius = IconSize),
                modifier = Modifier.align(Alignment.TopEnd),
            )
            HorizontalPagerIndicator(
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
                        onClick = onAddToCartClicked,
                        modifier = buttonModifier,
                    )
                } else {
                    SubscribeIconButton(
                        onClick = onSubscribeClicked,
                        modifier = buttonModifier,
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            val priceTextStyle = UiKitTheme.typography.caption1.regular
            val discountColor = UiKitTheme.colors.text.general.accent.red
            val originalPriceColor = if (product.price.hasDiscount) {
                UiKitTheme.colors.text.general.regular.disabled
            } else {
                UiKitTheme.colors.text.general.regular.default
            }
            val originalPriceTextDecoration = if (product.price.hasDiscount) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            }

            val originalPrice = stringResource(
                id = R.string.price_in_rubles_string,
                rememberFormattedPrice(product.price.originalPrice),
            )
            Text(
                text = originalPrice.uppercase(),
                style = priceTextStyle,
                color = originalPriceColor,
                textDecoration = originalPriceTextDecoration,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (product.price.hasDiscount) {
                Spacer(modifier = Modifier.width(8.dp))
                val currentPrice = stringResource(
                    id = R.string.price_in_rubles_string,
                    rememberFormattedPrice(product.price.currentPrice),
                )
                Text(
                    text = currentPrice.uppercase(),
                    style = priceTextStyle,
                    color = discountColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.discount_percent, product.price.discountPercent).uppercase(),
                    style = UiKitTheme.typography.caption2.regular,
                    color = discountColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Colors(
            colors = product.colors,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProductCardPlaceholder(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberSkeletonShimmer(),
) {
    Column(modifier = modifier) {
        Skeleton(
            shimmer = rememberSkeletonShimmer(width = 350.dp),
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
            val height = 16.dp
            Skeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .weight(1f)
                    .height(height),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Skeleton(
                shimmer = shimmer,
                modifier = Modifier.size(height),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val height = 10.dp
            Skeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .width(44.dp)
                    .height(height),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Skeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .width(48.dp)
                    .height(height),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Skeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .width(28.dp)
                    .height(height),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Skeleton(
            shimmer = shimmer,
            modifier = Modifier
                .padding(start = 16.dp)
                .width(40.dp)
                .height(8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// TODO: [Low] Extract?
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
        indication = rememberRipple(bounded = false, radius = IconSize),
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isAdded,
            label = "AddToCartIconButton",
        ) { isAdded ->
            val iconResId =
                if (isAdded) R.drawable.ic_cart_added_outline_24 else R.drawable.ic_cart_outline_24
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
        indication = rememberRipple(bounded = false, radius = IconSize),
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
                        ColorIcon(color = color.color.toComposeColor())
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
@FontScalePreviews
@DensityPreviews
@Composable
private fun ProductCardPreview(
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
@Composable
private fun ProductCardPreview() {
    ZarinaPreview {
        ProductCardPlaceholder(modifier = Modifier.background(Color.White))
    }
}

private const val MediaAspectRatio = 0.68f

private val IconSize: Dp get() = 16.dp

private val ColorSize: Dp get() = 8.dp
private val ColorSpacedBy: Dp get() = ColorSize
private val ColorsMoreTextWidth = 16.dp
