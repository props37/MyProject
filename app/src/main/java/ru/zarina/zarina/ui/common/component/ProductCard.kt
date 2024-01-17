package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductColor
import ru.zarina.zarina.domain.rework.product.currentPrice
import ru.zarina.zarina.ui.common.component.base.button.LikeIconButton
import ru.zarina.zarina.ui.common.component.base.button.ZarinaIconButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.common.tooling.preview.parameterprovider.ProductPreviewParameterProvider
import ru.zarina.zarina.ui.common.util.domain.toComposeColor
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.rememberEndlessPagerState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToFavoritesClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ImagePagerAspectRatio),
        ) {
            val pagerState = rememberEndlessPagerState(itemCount = product.media.size)

            ImagePager(
                pagerState = pagerState,
                media = remember(product.media) { product.media.toImmutableList() },
                modifier = Modifier.matchParentSize(),
            )
            LikeIconButton(
                isLiked = product.isInFavorites,
                onClick = onAddToFavoritesClicked,
                iconSize = 16.dp,
                indication = rememberRipple(bounded = false, radius = 16.dp),
                modifier = Modifier.align(Alignment.TopEnd),
            )
            // TODO: [High] Add pager indicator
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp),
        ) {
            Text(
                text = product.name,
                style = UiKitTheme.typographyReworked.tertiary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                AddToCartIconButton(
                    isAdded = product.isInCart,
                    onClick = onAddToCartClicked,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(28.dp),
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.price_in_rubles, product.price.currentPrice),
                style = UiKitTheme.typographyReworked.tertiary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (product.price.hasDiscount) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.price_in_rubles, product.price.originalPrice),
                    style = UiKitTheme.typographyReworked.tertiary.light,
                    color = UiKitTheme.colorsReworked.text.general.regular.disabled,
                    textDecoration = TextDecoration.LineThrough,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.discount_percent, product.price.discountPercent),
                    style = UiKitTheme.typographyReworked.caption2.bold,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Colors(
            colors = remember(product.colors) { product.colors.toImmutableList() },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(
    pagerState: PagerState,
    media: ImmutableList<Media>,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        // TODO: [High] Specify key. Do not use URLs as keys since there are no guarantee they are unique
        modifier = modifier,
    ) { page ->
        // TODO: [High] Implement
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colorsReworked.background.skeleton),
        )
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
    val iconSize = 16.dp
    ZarinaIconButton(
        onClick = onClick,
        isLoading = isLoading,
        indication = rememberRipple(bounded = false, radius = iconSize),
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
                tint = UiKitTheme.colorsReworked.icon.regular.default,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

// TODO: [Medium] Respect available width
@Composable
private fun Colors(
    colors: ImmutableList<ProductColor>,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ColorSize),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        colors.forEach {
            key(it.id.value) {
                Color(color = it.color.toComposeColor())
            }
        }
    }
}

@Composable
private fun Color(
    color: Color,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    val borderModifier = if (color.luminance() >= WhiteColorLuminanceThreshold) {
        Modifier.border(
            width = 0.5.dp,
            color = UiKitTheme.colorsReworked.border.general.disabled,
            shape = shape,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(ColorSize)
            .clip(shape)
            .background(color)
            .then(borderModifier),
    )
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
            modifier = Modifier.background(Color.White),
        )
    }
}

private const val ImagePagerAspectRatio = 0.68f

private const val WhiteColorLuminanceThreshold = 0.95f

private val ColorSize: Dp get() = 8.dp
