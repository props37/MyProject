package ru.zarina.zarina.ui.common.component.base.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.kotlin.valueAt
import kotlin.math.abs

// TODO: [High] Make scrollable?

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    segmentSize: DpSize = SegmentSize,
    segmentSpacedBy: Dp = 4.dp,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(segmentSpacedBy),
        modifier = modifier,
    ) {
        val currentPageIndexState = rememberUpdatedState(pagerState.currentPage % itemCount)

        for (pageIndex in 0 until itemCount) {
            Segment(
                progress = {
                    val currentPageIndex = currentPageIndexState.value
                    val currentPageOffsetFraction = pagerState.currentPageOffsetFraction
                    when {
                        currentPageIndex == pageIndex -> {
                            1f - abs(pagerState.currentPageOffsetFraction)
                        }

                        currentPageIndex == pageIndex - 1 && currentPageOffsetFraction > 0f -> {
                            abs(pagerState.currentPageOffsetFraction)
                        }

                        currentPageIndex == pageIndex + 1 && currentPageOffsetFraction < 0f -> {
                            abs(pagerState.currentPageOffsetFraction)
                        }

                        else -> 0f
                    }
                },
                modifier = Modifier.size(segmentSize),
            )
        }
    }
}

@Composable
private fun Segment(
    progress: () -> Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = (IndicatorSegmentInactiveAlpha..1f).valueAt(progress())
            }
            .clip(CircleShape)
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
    )
}

// TODO: [Low] Add preview

private const val IndicatorSegmentInactiveAlpha = 0.5f

private val SegmentSize: DpSize get() = DpSize(12.dp, 2.dp)
