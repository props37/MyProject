package ru.livetyping.zarina.core.ui.common.throttler

import android.os.SystemClock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

public class Throttler(delay: Duration) {
    private val delayMillis = delay.inWholeMilliseconds
    private var lastOperationTimestampMillis = 0L

    public fun throttle(operation: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastOperationTimestampMillis > delayMillis) {
            lastOperationTimestampMillis = SystemClock.elapsedRealtime()
            operation()
        }
    }

    public companion object {
        public fun getNavigationThrottler(): Throttler {
            return Throttler(NAVIGATION_THROTTLER_DELAY)
        }

        private val NAVIGATION_THROTTLER_DELAY
            get() = 500.milliseconds
    }
}
