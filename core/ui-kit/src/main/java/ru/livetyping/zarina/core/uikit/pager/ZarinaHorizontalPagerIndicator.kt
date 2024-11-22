package ru.livetyping.zarina.core.uikit.pager

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicatorDefaults.IndicatorSegmentInactiveAlpha
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicatorDefaults.ScrollTargetPageThreshold
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
public fun ZarinaHorizontalPagerIndicator(
    pagerState: PagerState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    segmentSize: DpSize = ZarinaHorizontalPagerIndicatorDefaults.SegmentSize,
    segmentSpacedBy: Dp = ZarinaHorizontalPagerIndicatorDefaults.SegmentSpacedBy,
    maxVisibleSegmentCount: Int = ZarinaHorizontalPagerIndicatorDefaults.MaxVisibleSegmentCount,
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
            .background(UiKitTheme.colors.background.general.regular.default)
    )
}

private fun ClosedFloatingPointRange<Float>.valueAt(progress: Float): Float {
    return (start + (endInclusive - start) * progress).coerceIn(this)
}

public object ZarinaHorizontalPagerIndicatorDefaults {
    public val SegmentSize: DpSize = DpSize(12.dp, 2.dp)

    public val SegmentSpacedBy: Dp = 4.dp

    public const val MaxVisibleSegmentCount: Int = 6

    internal const val IndicatorSegmentInactiveAlpha = 0.5f
    internal const val ScrollTargetPageThreshold = 2
}
