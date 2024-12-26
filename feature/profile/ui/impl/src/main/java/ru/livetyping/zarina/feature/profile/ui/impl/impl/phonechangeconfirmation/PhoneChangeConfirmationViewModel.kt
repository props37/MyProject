package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import ru.livetyping.zarina.core.platform.CountDownTimer
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.otp.NewOtpRequestState
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.otp.TextFieldOtpState
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationState
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
internal class PhoneChangeConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val smsCodeRetriever: SmsCodeRetriever,
) : ViewModel(), SideEffectSource<PhoneChangeConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private val countDownTimer = CountDownTimer()

    private val navEntry = savedStateHandle.toRoute<PhoneChangeConfirmationNavEntry>()
    private val phone = PhoneNumber.create(navEntry.phone)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val otpTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isOtpInvalid = MutableStateFlow(false)

    private val newOtpRequestState = MutableStateFlow<NewOtpRequestState>(
        NewOtpRequestState.Unavailable(NEW_OTP_REQUEST_TIMEOUT),
    )

    private val otpState: StateFlow<TextFieldOtpState> = combine(
        isOtpInvalid,
        newOtpRequestState,
        operationTracker.ongoingOperationKeys,
    ) { isOtpInvalid, newOtpRequestState, ongoingOperations ->
        TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = Operation.CONFIRM_PHONE_CHANGE in ongoingOperations,
            isInvalid = isOtpInvalid,
            newOtpRequestState = newOtpRequestState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = false,
            isInvalid = false,
            newOtpRequestState = NewOtpRequestState.Available,
        ),
    )

    private val visibleYandexCaptcha = MutableStateFlow<YandexCaptcha?>(null)

    val phoneChangeConfirmationState: StateFlow<PhoneChangeConfirmationState> = combine(
        otpState,
        visibleYandexCaptcha,
        operationTracker.ongoingOperationKeys,
    ) { otpState, visibleYandexCaptcha, ongoingOperations ->
        val isRequestNewOtpButtonLoading = Operation.REQUEST_NEW_OTP in ongoingOperations
                || visibleYandexCaptcha != null

        PhoneChangeConfirmationState(
            phone = phone,
            otpState = otpState,
            isRequestNewOtpButtonLoading = isRequestNewOtpButtonLoading,
            visibleYandexCaptcha = visibleYandexCaptcha,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PhoneChangeConfirmationState(
            phone = phone,
            otpState = otpState.value,
            isRequestNewOtpButtonLoading = false,
            visibleYandexCaptcha = null,
        ),
    )

    init {
        startNewOtpRequestTimeout()
        listenOtpSms()
        makeFieldsValidOnChange()
    }

    override fun onCleared() {
        smsCodeRetriever.stop()
    }

    // TODO: [Top] Implement
    fun onPhoneChangeConfirmationEvent(event: PhoneChangeConfirmationEvent) {
        when (event) {
            PhoneChangeConfirmationEvent.BackClicked -> onBackClicked()
            PhoneChangeConfirmationEvent.OtpEntered -> TODO()
            PhoneChangeConfirmationEvent.RequestNewOtpClicked -> TODO()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneChangeConfirmationScreenAction.BackClicked
            emitSideEffect(PhoneChangeConfirmationSideEffect.Navigate(action))
        }
    }

    private fun listenOtpSms() {
        smsCodeRetriever.addListener { otp ->
            otpTextFieldState.setTextAndPlaceCursorAtEnd(otp)
            // TODO: [Top] Implement
//            onOtpEntered()
        }
    }

    private fun makeFieldsValidOnChange() {
        otpTextFieldState.textAsFlow()
            .onEach { isOtpInvalid.value = false }
            .launchIn(viewModelScope)
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

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PhoneChangeConfirmationSideEffect.ShowZarinaToast(message))
    }

    private enum class Operation : OperationKey { CONFIRM_PHONE_CHANGE, REQUEST_NEW_OTP }

    private companion object {
        private val NEW_OTP_REQUEST_TIMEOUT get() = 1.minutes
    }
}
