package ru.livetyping.zarina.ui.screen.common.otp

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.domain.common.exception.OtpException
import ru.livetyping.zarina.ui.base.viewmodel.ViewModelComponent
import ru.livetyping.zarina.ui.common.countdowntimer.CountdownTimer
import ru.livetyping.zarina.ui.common.otp.OtpResendState
import ru.livetyping.zarina.ui.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class OtpViewModelComponent @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModelComponent() {

    private val operationTracker = OperationTracker()

    private val countdownTimer = CountdownTimer()

    private var confirmOtpJob: Job? = null
    private var resendOtpJob: Job? = null

    private val otpValueHolder = savedStateHandle.createValueHolder(
        key = KEY_OTP,
        initialValue = "",
    )

    val otp: StateFlow<String> = otpValueHolder.stateFlow

    val isOtpLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.CONFIRM_OTP)
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    private val _isOtpInvalid = MutableStateFlow(false)
    val isOtpInvalid: StateFlow<Boolean> = _isOtpInvalid.asStateFlow()

    private val _otpResendState =
        MutableStateFlow<OtpResendState>(OtpResendState.TimeoutCountdown(OTP_RESEND_INITIAL_TIMEOUT))
    val otpResendState: StateFlow<OtpResendState> = _otpResendState.asStateFlow()

    init {
        startOtpResendCountdownTimer()
    }

    fun onOtpChanged(otp: String) {
        otpValueHolder.set(otp)
        _isOtpInvalid.value = false
    }

    fun onOtpEntered(block: suspend () -> Result<*>) {
        if (confirmOtpJob?.isActive == true) return
        confirmOtpJob = scope.launch {
            operationTracker.track(Operation.CONFIRM_OTP) {
                block()
                    .onFailure {
                        if (it is OtpException) {
                            _isOtpInvalid.value = true
                            otpValueHolder.set("")
                        }
                    }
            }
        }
    }

    fun onResendOtpClicked(block: suspend () -> Result<*>) {
        if (resendOtpJob?.isActive == true) return
        resendOtpJob = scope.launch {
            block()
                .onSuccess {
                    startOtpResendCountdownTimer()
                }
        }
    }

    private fun startOtpResendCountdownTimer() {
        countdownTimer.start(
            duration = OTP_RESEND_INITIAL_TIMEOUT,
            onTick = { remainingTime ->
                _otpResendState.value = OtpResendState.TimeoutCountdown(remainingTime)
            },
            onFinish = {
                _otpResendState.value = OtpResendState.ResendAvailable
            },
        )
    }

    private enum class Operation : OperationKey { CONFIRM_OTP }

    companion object {
        private const val KEY_OTP = "otp"

        private val OTP_RESEND_INITIAL_TIMEOUT: Duration get() = 1.minutes
    }
}
