package ru.livetyping.zarina.util.compose.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> PagerTabRowIntegration(
    pagerState: PagerState,
    tabs: List<T>,
    currentTab: T,
    onCurrentTabChanged: (T) -> Unit,
) {
    val updatedCurrentTab by rememberUpdatedState(currentTab)
    val updatedOnCurrentTabChanged by rememberUpdatedState(onCurrentTabChanged)

    LaunchedEffect(pagerState, tabs) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                val newCurrentTab = tabs.getOrNull(currentPage)
                if (newCurrentTab != null) {
                    updatedOnCurrentTabChanged(newCurrentTab)
                } else {
                    Timber.e("Could not find a tab for Pager page $currentPage")
                }
            }
    }

    LaunchedEffect(pagerState, tabs) {
        snapshotFlow { updatedCurrentTab }
            .collectLatest { currentTab ->
                val currentPage = tabs.indexOf(currentTab)
                if (currentPage != -1) {
                    pagerState.animateScrollToPage(currentPage)
                }
            }
    }
}
