package ru.zarina.zarina.ui.common.util.library.paging

import android.os.SystemClock
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.snapshotFlow
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

suspend fun <T : Any> LazyPagingItems<T>.retryAppendPrependErrors(
    lazyGridState: LazyGridState,
    attemptDelayMillis: Long = ATTEMPT_DELAY_MILLIS,
) {
    coroutineScope {
        launch {
            retryAppendErrors(lazyGridState, attemptDelayMillis)
        }
        launch {
            retryPrependErrors(lazyGridState, attemptDelayMillis)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : Any> LazyPagingItems<T>.retryAppendErrors(
    lazyGridState: LazyGridState,
    attemptDelayMillis: Long = ATTEMPT_DELAY_MILLIS,
) {
    var lastAttemptTimestamp = 0L

    val lastVisibleItemIndexFlow = snapshotFlow {
        lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
    }

    snapshotFlow { this.loadState.append }
        .map { it is LoadState.Error }
        .distinctUntilChanged()
        .flatMapLatest { isError ->
            if (isError) lastVisibleItemIndexFlow else emptyFlow()
        }
        .distinctUntilChanged()
        .collect { index ->
            if (SystemClock.elapsedRealtime() - lastAttemptTimestamp >= attemptDelayMillis) {
                if (index != null) {
                    val item = this.peek(index)
                    if (item == null) {
                        lastAttemptTimestamp = SystemClock.elapsedRealtime()
                        Timber.v("Retry append error")
                        this.retry()
                    }
                }
            }
        }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : Any> LazyPagingItems<T>.retryPrependErrors(
    lazyGridState: LazyGridState,
    attemptDelayMillis: Long = ATTEMPT_DELAY_MILLIS,
) {
    var lastAttemptTimestamp = 0L

    val firstVisibleItemIndexFlow = snapshotFlow { lazyGridState.firstVisibleItemIndex }

    snapshotFlow { this.loadState.prepend }
        .map { it is LoadState.Error }
        .distinctUntilChanged()
        .flatMapLatest { isError ->
            if (isError) firstVisibleItemIndexFlow else emptyFlow()
        }
        .distinctUntilChanged()
        .collect { index ->
            if (SystemClock.elapsedRealtime() - lastAttemptTimestamp >= attemptDelayMillis) {
                val item = this.peek(index)
                if (item == null) {
                    Timber.v("Retry prepend error")
                    lastAttemptTimestamp = SystemClock.elapsedRealtime()
                    this.retry()
                }
            }
        }
}

private const val ATTEMPT_DELAY_MILLIS = 3_000L
