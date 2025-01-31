package ru.livetyping.zarina.core.uicomponent.otp

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.platform.CountDownTimer
import ru.livetyping.zarina.core.uicommon.otp.NewOtpRequestState
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState
import ru.livetyping.zarina.core.uicompose.textAsFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

public class OtpStateComponent(
    savedStateHandle: SavedStateHandle,
    private val coroutineScope: CoroutineScope,
    private val defaultNewOtpRequestTimeout: Duration = NEW_OTP_REQUEST_TIMEOUT_DEFAULT_VALUE,
) {
    private val countDownTimer = CountDownTimer()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val otpTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isOtpLoading = MutableStateFlow(false)

    private val isOtpInvalid = MutableStateFlow(false)

    private val newOtpRequestState = MutableStateFlow<NewOtpRequestState>(
        NewOtpRequestState.Unavailable(defaultNewOtpRequestTimeout),
    )

    private val isRequestNewOtpButtonLoading = MutableStateFlow(false)

    public val otpState: StateFlow<TextFieldOtpState> = combine(
        isOtpLoading,
        isOtpInvalid,
        newOtpRequestState,
        isRequestNewOtpButtonLoading,
    ) { isOtpLoading, isOtpInvalid, newOtpRequestState, isRequestNewOtpButtonLoading ->
        TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = isOtpLoading,
            isInvalid = isOtpInvalid,
            newOtpRequestState = newOtpRequestState,
            isRequestNewOtpButtonLoading = isRequestNewOtpButtonLoading,
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = false,
            isInvalid = false,
            newOtpRequestState = NewOtpRequestState.Available,
            isRequestNewOtpButtonLoading = false,
        ),
    )

    init {
        startNewOtpRequestTimeout()
        makeOtpValidOnChange()
    }

    public fun setOtp(otp: String) {
        otpTextFieldState.setTextAndPlaceCursorAtEnd(otp)
    }

    public fun setIsOtpLoading(isLoading: Boolean) {
        isOtpLoading.value = isLoading
    }

    public fun setIsOtpInvalid(isInvalid: Boolean) {
        isOtpInvalid.value = isInvalid
    }

    public fun setIsRequestNewOtpButtonLoading(isLoading: Boolean) {
        isRequestNewOtpButtonLoading.value = isLoading
    }

    public fun startNewOtpRequestTimeout(timeout: Duration = defaultNewOtpRequestTimeout) {
        countDownTimer.start(
            duration = timeout,
            onTick = { remainingTime ->
                newOtpRequestState.value = NewOtpRequestState.Unavailable(remainingTime)
            },
            onFinish = {
                newOtpRequestState.value = NewOtpRequestState.Available
            },
        )
    }

    private fun makeOtpValidOnChange() {
        otpTextFieldState.textAsFlow()
            .onEach { isOtpInvalid.value = false }
            .launchIn(coroutineScope)
    }

    private companion object {
        private val NEW_OTP_REQUEST_TIMEOUT_DEFAULT_VALUE get() = 1.minutes
    }
}
