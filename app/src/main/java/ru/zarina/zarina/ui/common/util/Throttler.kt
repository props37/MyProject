package ru.zarina.zarina.ui.common.util

import ru.zarina.zarina.base.throttler.Throttler
import kotlin.time.Duration.Companion.milliseconds

val DELAY_NAVIGATION = 500.milliseconds

fun Throttler.Companion.getNavigationThrottler(): Throttler {
    return Throttler(DELAY_NAVIGATION)
}
