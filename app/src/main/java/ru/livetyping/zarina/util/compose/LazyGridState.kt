package ru.livetyping.zarina.util.compose

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import kotlin.math.abs

@Composable
fun LazyGridState.collectIsScrollingBackwardAsState(): State<Boolean> {
    val prevFirstVisibleItemIndex = remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    val prevFirstVisibleItemScrollOffset =
        remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (prevFirstVisibleItemIndex.intValue != firstVisibleItemIndex) {
                prevFirstVisibleItemIndex.intValue > firstVisibleItemIndex
            } else {
                prevFirstVisibleItemScrollOffset.intValue >= firstVisibleItemScrollOffset
            }.also {
                prevFirstVisibleItemIndex.intValue = firstVisibleItemIndex
                prevFirstVisibleItemScrollOffset.intValue = firstVisibleItemScrollOffset
            }
        }
    }
}

suspend fun LazyGridState.animateFastScrollToItem(
    item: Int,
    distanceThreshold: Int,
    scrollOffset: Int = 0,
) {
    val absoluteDistance = abs(firstVisibleItemIndex - item)
    if (absoluteDistance > distanceThreshold) {
        val fastScrollTargetItem = if (firstVisibleItemIndex - item > 0) {
            item + distanceThreshold
        } else {
            item - distanceThreshold
        }
        scrollToItem(fastScrollTargetItem, scrollOffset)
    }
    animateScrollToItem(item, scrollOffset)
}
