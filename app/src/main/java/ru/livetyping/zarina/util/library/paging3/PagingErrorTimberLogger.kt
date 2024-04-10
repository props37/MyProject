package ru.livetyping.zarina.util.library.paging3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.livetyping.zarina.util.library.timber.isEnabled
import timber.log.Timber

@Composable
fun PagingErrorTimberLogger(pagingItems: LazyPagingItems<*>) {
    if (Timber.isEnabled) {
        LaunchedEffect(pagingItems) {
            snapshotFlow { pagingItems.loadState }
                .collect { loadStates ->
                    val refresh = loadStates.refresh
                    if (refresh is LoadState.Error) Timber.e(refresh.error)

                    val append = loadStates.append
                    if (append is LoadState.Error) Timber.e(append.error)

                    val prepend = loadStates.prepend
                    if (prepend is LoadState.Error) Timber.e(prepend.error)
                }
        }
    }
}
