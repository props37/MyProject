package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.datasource.cache.Cache
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.ui.common.components.DiscountBadge
import ru.zarina.zarina.ui.common.components.FavoriteHeart
import ru.zarina.zarina.ui.common.components.InvertedRippleTheme
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.components.PageDots
import ru.zarina.zarina.ui.common.components.rememberInfinitePagerState
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaSection(
    product: Product,
    isFavoriteShaking: Boolean,
    onFavoriteChange: (Boolean) -> Unit,
    onBuyCompleteLookClick: () -> Unit,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val pagerState = rememberInfinitePagerState(itemCount = product.media.size)
        MediaPager(
            media = product.media,
            cache = cache,
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        )
        val coroutineScope = rememberCoroutineScope()
        PageDots(
            count = product.media.size,
            activeIndex = pagerState.currentPage % product.media.size,
            onDotClick = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        DiscountBadge(
            price = product.price,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            if (product.isLookPart)
                BuyCompleteLookButton(
                    onClick = onBuyCompleteLookClick,
                    modifier = Modifier

                )

            var isFavorite by remember(product.isFavorite, isFavoriteShaking) {
                mutableStateOf(product.isFavorite)
            }

            FavoriteHeart(
                isFavorite = isFavorite,
                isShaking = isFavoriteShaking,
                onFavoriteChange = {
                    isFavorite = it
                    onFavoriteChange(it)
                },
            )
        }
    }
}

@Composable
private fun BuyCompleteLookButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalRippleTheme provides InvertedRippleTheme) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .aspectRatio(1f)
                    .background(color = UiKitTheme.colorsOld.productBadgeBackground),
            ) {
                Text(
                    text = stringResource(R.string.buy_complete_look),
                    color = UiKitTheme.colorsOld.productBadgeForeground,
                    style = UiKitTheme.typographyOld.circle1012,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(2.dp)
                        .width(IntrinsicSize.Min),
                )
            }
        }
    }
}
