package ru.livetyping.zarina.presentation.common.component.pager

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.pager.rememberEndlessPagerState
import ru.livetyping.zarina.util.kotlin.loopingGet
import ru.livetyping.zarina.util.kotlin.valueAt
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ZarinaHorizontalPagerIndicator(
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
            .background(UiKitTheme.colors.background.general.regular.default)
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        val colors = remember {
            List(10) { if (it % 2 == 0) Color.Black else Color.DarkGray }
        }

        val pagerState = rememberEndlessPagerState(itemCount = colors.size)

        Column {
            Box {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.size(300.dp),
                ) { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors.loopingGet(page) ?: Color.White),
                    )
                }

                ZarinaHorizontalPagerIndicator(
                    pagerState = pagerState,
                    itemCount = colors.size,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp),
                )
            }

            val coroutineScope = rememberCoroutineScope()
            Row {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                ) {
                    Text(text = "Prev")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

private const val IndicatorSegmentInactiveAlpha = 0.5f
private const val ScrollTargetPageThreshold = 2

private val SegmentSize: DpSize get() = DpSize(12.dp, 2.dp)
