package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.platform.CountDownTimer
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.otp.NewOtpRequestState
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
internal class OtpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: OtpDeps,
) : ViewModel(), SideEffectSource<OtpSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val countDownTimer = CountDownTimer()

    private val navEntry = savedStateHandle.toRoute<OtpNavEntry>()
    private val phone = PhoneNumber.create(navEntry.phone)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val otpTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isOtpLoading = MutableStateFlow(false)

    private val isOtpInvalid = MutableStateFlow(false)

    private val newOtpRequestState = MutableStateFlow<NewOtpRequestState>(
        NewOtpRequestState.Unavailable(NEW_OTP_REQUEST_TIMEOUT),
    )

    val otpState: StateFlow<TextFieldOtpState> = combine(
        isOtpLoading,
        isOtpInvalid,
        newOtpRequestState,
    ) { isOtpLoading, isOtpInvalid, newOtpRequestState ->
        TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = isOtpLoading,
            isInvalid = isOtpInvalid,
            newOtpRequestState = newOtpRequestState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = false,
            isInvalid = false,
            newOtpRequestState = NewOtpRequestState.Available,
        ),
    )

    init {
        startNewOtpRequestTimeout()
        listenOtpSms()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = OtpScreenAction.BackClicked
            emitSideEffect(OtpSideEffect.Navigate(action))
        }
    }

    private fun listenOtpSms() {
        deps.smsCodeRetriever.addListener { otp ->
            otpTextFieldState.setTextAndPlaceCursorAtEnd(otp)
            // TODO: [Top] Use OTP
            // TODO: [Top] Hide keyboard
        }
    }

    private fun startNewOtpRequestTimeout() {
        countDownTimer.start(
            duration = NEW_OTP_REQUEST_TIMEOUT,
            onTick = { remainingTime ->
                newOtpRequestState.value = NewOtpRequestState.Unavailable(remainingTime)
            },
            onFinish = {
                newOtpRequestState.value = NewOtpRequestState.Available
            },
        )
    }

    private companion object {
        private val NEW_OTP_REQUEST_TIMEOUT get() = 1.minutes
    }
}
