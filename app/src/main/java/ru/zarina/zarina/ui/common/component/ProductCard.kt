package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.base.button.LikeIconButton
import ru.zarina.zarina.ui.common.component.base.button.ZarinaIconButton
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ImagePagerAspectRatio),
        ) {
            val pagerState = rememberPagerState { 0 }

            ImagePager(
                pagerState = pagerState,
                modifier = Modifier.matchParentSize(),
            )

            LikeIconButton(
                isLiked = false, // TODO: [High] Implement
                onClick = { /*TODO*/ },
                iconSize = 16.dp,
                indication = rememberRipple(bounded = false, radius = 16.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd),
            )

            // TODO: [High] Add pager indicator
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 4.dp),
        ) {
            Text(
                text = "Свитер из вискозы", // TODO: [High] Implement
                style = UiKitTheme.typographyReworked.tertiary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            AddToCartIconButton(
                isAdded = false, // TODO: [High] Implement
                onClick = { /*TODO*/ },
                modifier = Modifier.size(32.dp),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Text(
                text = "2 599 ₽", // TODO: [High] Implement
                style = UiKitTheme.typographyReworked.tertiary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "3 599 ₽", // TODO: [High] Implement
                style = UiKitTheme.typographyReworked.tertiary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.disabled,
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "−27%", // TODO: [High] Implement
                style = UiKitTheme.typographyReworked.caption2.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // TODO: [High] Add colors
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    // TODO: [High] Specify key
    HorizontalPager(
        state = pagerState,
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
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AddToCartIconButton(
    isAdded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
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
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun ProductCardPreview() {
    ZarinaPreview {
        ProductCard(modifier = Modifier.background(Color.White))
    }
}

private const val ImagePagerAspectRatio = 0.69f
