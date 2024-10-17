package ru.livetyping.zarina.core.uicompose.pager

import androidx.annotation.FloatRange
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
public fun <T> rememberPagerConnectedToTabRowState(
    tabs: List<T>,
    currentTab: T,
    onTabChanged: (T) -> Unit,
    initialPage: Int = 0,
    @FloatRange(from = -0.5, to = 0.5)
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): PagerState {
    val updatedCurrentTab by rememberUpdatedState(currentTab)
    val updatedOnTabChanged by rememberUpdatedState(onTabChanged)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = initialPageOffsetFraction,
        pageCount = pageCount,
    )

    LaunchedEffect(tabs) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                val newCurrentTab = tabs.getOrNull(currentPage)
                if (newCurrentTab != null) {
                    updatedOnTabChanged(newCurrentTab)
                }
            }
    }

    LaunchedEffect(tabs) {
        snapshotFlow { updatedCurrentTab }
            .collectLatest { currentTab ->
                val currentPage = tabs.indexOf(currentTab)
                if (currentPage != -1) {
                    pagerState.animateScrollToPage(currentPage)
                }
            }
    }

    return pagerState
}
