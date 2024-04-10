package ru.livetyping.zarina.base.throttler

import android.os.SystemClock
import kotlin.time.Duration

class Throttler(delay: Duration) {
    private val delayMillis = delay.inWholeMilliseconds
    private var lastOperationTimestamp = 0L

    fun throttle(operation: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastOperationTimestamp > delayMillis) {
            lastOperationTimestamp = SystemClock.elapsedRealtime()
            operation()
        }
    }

    companion object
}
