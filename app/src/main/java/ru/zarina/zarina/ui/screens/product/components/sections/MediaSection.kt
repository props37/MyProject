package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.datasource.cache.Cache
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.components.DiscountBadge
import ru.zarina.zarina.ui.common.components.MediaPager
import ru.zarina.zarina.ui.common.components.PageDots

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaSection(
    product: Product,
    cache: State<Cache?>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val pagerState = rememberPagerState()
        MediaPager(
            media = product.media,
            cache = cache,
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        )
        val coroutineScope = rememberCoroutineScope()
        PageDots(
            count = product.media.size,
            activeIndex = pagerState.currentPage,
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
    }
}
