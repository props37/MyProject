package ru.livetyping.zarina.presentation.common.navigation

import android.os.SystemClock
import kotlinx.coroutines.delay

// Workaround for bug which caused NavController to throw ConcurrentModificationException
// when navigating back too fast after activity lifecycle moved to STARTED state
// See: https://issuetracker.google.com/issues/347737880
suspend inline fun safeNavigate(startedElapsedRealtimeMillis: Long, action: () -> Unit) {
    val currentElapsedTimestampMillis = SystemClock.elapsedRealtime()
    val millisSinceResumed = currentElapsedTimestampMillis - startedElapsedRealtimeMillis
    if (millisSinceResumed < SAFE_NAVIGATION_DELAY) {
        delay(SAFE_NAVIGATION_DELAY - millisSinceResumed)
    }
    action()
}

const val SAFE_NAVIGATION_DELAY = 500
