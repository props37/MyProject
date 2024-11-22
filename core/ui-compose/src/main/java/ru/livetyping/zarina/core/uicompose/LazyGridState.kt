package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.lazy.grid.LazyGridState
import kotlin.math.abs

public suspend fun LazyGridState.animateFastScrollToItem(
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
