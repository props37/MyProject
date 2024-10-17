package ru.livetyping.zarina.core.navigationutil

import android.os.SystemClock
import kotlinx.coroutines.delay

/**
 * Wrapper for Jetpack Navigation calls to be used as a workaround for a bug which causes
 * `NavController` to throw [ConcurrentModificationException] if the navigation gets called
 * too soon after the lifecycle reaches `STARTED` state.
 *
 * See this [IssueTracker](https://issuetracker.google.com/issues/347737880) thread for more info.
 */
public suspend inline fun safeNavigate(startedElapsedRealtime: Long, action: () -> Unit) {
    val currentElapsedRealtime = SystemClock.elapsedRealtime()
    val millisSinceStarted = currentElapsedRealtime - startedElapsedRealtime
    if (millisSinceStarted < SAFE_NAVIGATION_DELAY_MILLIS) {
        delay(SAFE_NAVIGATION_DELAY_MILLIS - millisSinceStarted)
    }
    action()
}

public const val SAFE_NAVIGATION_DELAY_MILLIS: Long = 500L
