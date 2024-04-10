package ru.livetyping.zarina.ui.common.countdowntimer

import android.os.CountDownTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class CountdownTimer {
    private var countdownTimerImpl: CountDownTimer? = null

    @Synchronized
    fun start(
        duration: Duration,
        tick: Duration = 1.seconds,
        onTick: (remainingTime: Duration) -> Unit,
        onFinish: () -> Unit,
    ) {
        countdownTimerImpl = object : CountDownTimer(
            /* millisInFuture = */ duration.inWholeMilliseconds,
            /* countDownInterval = */ tick.inWholeMilliseconds,
        ) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished.milliseconds)
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    @Synchronized
    fun cancel() {
        countdownTimerImpl?.cancel()
        countdownTimerImpl = null
    }
}
