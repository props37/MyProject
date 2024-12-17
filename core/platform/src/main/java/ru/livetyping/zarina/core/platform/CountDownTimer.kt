package ru.livetyping.zarina.core.platform

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import android.os.CountDownTimer as PlatformCountDownTimer

public class CountDownTimer {
    private var countDownTimerImpl: PlatformCountDownTimer? = null

    public fun start(
        duration: Duration,
        tick: Duration = 1.seconds,
        onTick: (remainingTime: Duration) -> Unit,
        onFinish: () -> Unit,
    ) {
        if (countDownTimerImpl != null) cancel()

        countDownTimerImpl = object : PlatformCountDownTimer(
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

    public fun cancel() {
        countDownTimerImpl?.cancel()
        countDownTimerImpl = null
    }
}
