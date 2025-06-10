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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.flow.collectLatest
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicatorDefaults.IndicatorSegmentInactiveAlpha
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicatorDefaults.ScrollTargetPageThreshold
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
public fun ZarinaHorizontalPagerIndicator(
    pagerState: PagerState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    style: ZarinaHorizontalPagerIndicatorStyle = ZarinaHorizontalPagerIndicatorStyle.Dots(),
    maxVisibleSegmentCount: Int = ZarinaHorizontalPagerIndicatorDefaults.MaxVisibleSegmentCount,
) {
    val updatedDensity = rememberUpdatedState(LocalDensity.current)

    val maxWidth = remember(style, maxVisibleSegmentCount) {
        maxVisibleSegmentCount * (style.segmentSize.width + style.spacedBy)
    }

    val scrollState = rememberScrollState()
    val scrollAnimationSpec = remember { spring<Float>(stiffness = Spring.StiffnessMediumLow) }

    LaunchedEffect(pagerState, style, maxVisibleSegmentCount, itemCount) {
        val segmentWidthPx = with(updatedDensity.value) { style.segmentSize.width.toPx() }
        val segmentSpacedByPx = with(updatedDensity.value) { style.spacedBy.toPx() }
        snapshotFlow { pagerState.currentPage % itemCount }
            .collectLatest { page ->
                val scrollTargetPage = (page - (maxVisibleSegmentCount - ScrollTargetPageThreshold))
                val scrollValue =
                    scrollTargetPage * (segmentWidthPx + segmentSpacedByPx).roundToInt()
                scrollState.animateScrollTo(scrollValue, scrollAnimationSpec)
            }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(style.spacedBy),
        modifier = modifier
            .widthIn(max = maxWidth)
            .horizontalScroll(scrollState, enabled = false),
    ) {
        for (pageIndex in 0 until itemCount) {
            Segment(
                style = style,
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
                modifier = Modifier.size(style.segmentSize),
            )
        }
    }
}

@Composable
private fun Segment(
    style: ZarinaHorizontalPagerIndicatorStyle,
    progress: () -> Float,
    modifier: Modifier = Modifier,
) {
    val shape = when (style) {
        is ZarinaHorizontalPagerIndicatorStyle.Dots -> CircleShape
        is ZarinaHorizontalPagerIndicatorStyle.Rectangles -> RectangleShape
    }
    val color = when (style) {
        is ZarinaHorizontalPagerIndicatorStyle.Dots -> UiKitTheme2.colors.mainBlack
        is ZarinaHorizontalPagerIndicatorStyle.Rectangles -> UiKitTheme2.colors.white
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                alpha = (IndicatorSegmentInactiveAlpha..1f).valueAt(progress())
            }
            .clip(shape)
            .background(color)
    )
}

private fun ClosedFloatingPointRange<Float>.valueAt(progress: Float): Float {
    return (start + (endInclusive - start) * progress).coerceIn(this)
}

@Stable
public sealed class ZarinaHorizontalPagerIndicatorStyle {
    public abstract val segmentSize: DpSize
    public abstract val spacedBy: Dp

    @Immutable
    public data class Dots(
        val dotSize: Dp = 4.dp,
        override val spacedBy: Dp = 3.dp,
    ) : ZarinaHorizontalPagerIndicatorStyle() {
        override val segmentSize: DpSize get() = DpSize(dotSize, dotSize)
    }

    @Immutable
    public data class Rectangles(
        override val segmentSize: DpSize = DpSize(12.dp, 2.dp),
        override val spacedBy: Dp = 2.dp,
    ) : ZarinaHorizontalPagerIndicatorStyle()
}

public object ZarinaHorizontalPagerIndicatorDefaults {
    internal const val MaxVisibleSegmentCount = 6

    internal const val IndicatorSegmentInactiveAlpha = 0.5f
    internal const val ScrollTargetPageThreshold = 2
}
