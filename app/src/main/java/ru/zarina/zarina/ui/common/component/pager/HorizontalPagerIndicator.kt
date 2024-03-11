package ru.zarina.zarina.ui.common.component.pager

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.flow.collectLatest
import ru.zarina.zarina.ui.theme.old.UiKitTheme
import ru.zarina.zarina.util.kotlin.valueAt
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    segmentSize: DpSize = SegmentSize,
    segmentSpacedBy: Dp = 4.dp,
    maxVisibleSegmentCount: Int = 6,
) {
    val updatedDensity = rememberUpdatedState(LocalDensity.current)

    val maxWidth = remember(segmentSize, segmentSpacedBy, maxVisibleSegmentCount) {
        maxVisibleSegmentCount * (segmentSize.width + segmentSpacedBy)
    }

    val scrollState = rememberScrollState()
    val scrollAnimationSpec = remember { spring<Float>(stiffness = Spring.StiffnessMediumLow) }

    LaunchedEffect(pagerState, segmentSize, segmentSpacedBy, maxVisibleSegmentCount, itemCount) {
        val segmentWidthPx = with(updatedDensity.value) { segmentSize.width.toPx() }
        val segmentSpacedByPx = with(updatedDensity.value) { segmentSpacedBy.toPx() }
        snapshotFlow { pagerState.currentPage % itemCount }
            .collectLatest { page ->
                val scrollTargetPage = (page - (maxVisibleSegmentCount - ScrollTargetPageThreshold))
                val scrollValue =
                    scrollTargetPage * (segmentWidthPx + segmentSpacedByPx).roundToInt()
                scrollState.animateScrollTo(scrollValue, scrollAnimationSpec)
            }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(segmentSpacedBy),
        modifier = modifier
            .widthIn(max = maxWidth)
            .horizontalScroll(scrollState, enabled = false),
    ) {
        for (pageIndex in 0 until itemCount) {
            Segment(
                progress = {
                    val currentPageOffsetFraction = pagerState.currentPageOffsetFraction
                    when {
                        pagerState.currentPage % itemCount == pageIndex -> {
                            1f - abs(pagerState.currentPageOffsetFraction)
                        }

                        (pagerState.currentPage + 1) % itemCount == pageIndex
                                && currentPageOffsetFraction > 0f -> {
                            abs(pagerState.currentPageOffsetFraction)
                        }

                        (pagerState.currentPage - 1) % itemCount == pageIndex
                                && currentPageOffsetFraction < 0f -> {
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
private const val ScrollTargetPageThreshold = 2

private val SegmentSize: DpSize get() = DpSize(12.dp, 2.dp)
