package ru.livetyping.zarina.util.compose

import androidx.compose.foundation.lazy.LazyListState
import kotlin.math.abs

suspend fun LazyListState.animateFastScrollToItem(
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
