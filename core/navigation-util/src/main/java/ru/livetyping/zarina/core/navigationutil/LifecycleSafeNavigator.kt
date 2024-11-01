package ru.livetyping.zarina.core.navigationutil

import android.os.SystemClock
import kotlinx.coroutines.delay

/**
 * Wrapper for Jetpack Navigation calls to be used as a workaround for a bug which causes
 * `NavController` to throw [ConcurrentModificationException] if the navigation gets called
 * too soon after the lifecycle reaches `STARTED` state.
 *
 * See this [IssueTracker](https://issuetracker.google.com/issues/347737880) thread for more info.
 *
 * Use [onLifecycleStartedEvent] to update internal Lifecycle STARTED time, otherwise value
 * created upon instance initialization will be used.
 */
public class LifecycleSafeNavigator {
    private var lifecycleStartedElapsedTime = getCurrentElapsedTime()

    public suspend fun safeNavigate(action: () -> Unit) {
        val currentElapsedTime = getCurrentElapsedTime()
        val millisSinceStarted = currentElapsedTime - lifecycleStartedElapsedTime
        if (millisSinceStarted < SAFE_NAVIGATION_DELAY_MILLIS) {
            delay(SAFE_NAVIGATION_DELAY_MILLIS - millisSinceStarted)
        }
        action()
    }

    public fun onLifecycleStartedEvent() {
        lifecycleStartedElapsedTime = getCurrentElapsedTime()
    }

    private fun getCurrentElapsedTime(): Long {
        return SystemClock.elapsedRealtime()
    }

    private companion object {
        const val SAFE_NAVIGATION_DELAY_MILLIS: Long = 500L
    }
}
