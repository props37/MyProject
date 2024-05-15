package ru.livetyping.zarina.presentation.common.component.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import ru.livetyping.zarina.presentation.common.component.pullrefresh.ZarinaPullRefreshIndicator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ZarinaPagingPullRefreshContainer(
    loadState: CombinedLoadStates,
    onPullRefreshTriggered: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val updatedRefreshLoadState by rememberUpdatedState(loadState.refresh)

    Box(modifier = modifier) {
        val isPullRefreshTriggered = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            snapshotFlow { updatedRefreshLoadState }.collect {
                if (it !is LoadState.Loading) {
                    isPullRefreshTriggered.value = false
                }
            }
        }

        val isRefreshing = remember {
            derivedStateOf { updatedRefreshLoadState is LoadState.Loading }
        }

        val isPullRefreshing by remember {
            derivedStateOf { isPullRefreshTriggered.value && isRefreshing.value }
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isPullRefreshing,
            onRefresh = onPullRefreshTriggered,
        )

        ZarinaPullRefreshIndicator(
            refreshing = isPullRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
        )

        Box(
            modifier = Modifier.pullRefresh(pullRefreshState),
            content = content,
        )
    }
}
