package ru.zarina.zarina.ui.common.base

import android.os.SystemClock
import androidx.annotation.MainThread
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class Throttler(delay: Duration) {
    private val delayMillis = delay.inWholeMilliseconds
    private var lastOperationTimestamp = 0L

    @MainThread
    fun throttle(operation: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastOperationTimestamp > delayMillis) {
            lastOperationTimestamp = SystemClock.elapsedRealtime()
            operation()
        }
    }

    companion object {
        val DELAY_NAVIGATION = 500.milliseconds
    }
}
