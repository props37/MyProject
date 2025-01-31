package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.user.exception.OtpException
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewAuthOtpUseCase
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
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaState
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.model.PhoneConfirmationEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation.model.PhoneConfirmationState
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [High] Refactor. Follow DRY. Extract common code and reuse it.
@HiltViewModel
internal class PhoneConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: PhoneConfirmationDeps,
) : ViewModel(), SideEffectSource<PhoneConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var confirmPhoneJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val countDownTimer = CountDownTimer()

    private val navEntry = savedStateHandle.toRoute<PhoneConfirmationNavEntry>()
    private val phone = navEntry.getPhone()

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
            isLoading = Operation.CONFIRM_PHONE in ongoingOperations,
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

    private val _yandexCaptchaState = MutableStateFlow<YandexCaptchaState>(YandexCaptchaState.None)
    val yandexCaptchaState: StateFlow<YandexCaptchaState> = _yandexCaptchaState.asStateFlow()

    val phoneConfirmationState: StateFlow<PhoneConfirmationState> = combine(
        otpState,
        yandexCaptchaState,
        operationTracker.ongoingOperationKeys,
    ) { otpState, yandexCaptchaState, ongoingOperations ->
        val isRequestNewOtpButtonLoading = Operation.REQUEST_NEW_OTP in ongoingOperations
                || yandexCaptchaState is YandexCaptchaState.Started

        PhoneConfirmationState(
            phone = phone,
            otpState = otpState,
            isRequestNewOtpButtonLoading = isRequestNewOtpButtonLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PhoneConfirmationState(
            phone = phone,
            otpState = otpState.value,
            isRequestNewOtpButtonLoading = false,
        ),
    )

    init {
        startNewOtpRequestTimeout()
        listenOtpSms()
        makeFieldsValidOnChange()
    }

    override fun onCleared() {
        deps.smsCodeRetriever.stop()
    }

    fun onPhoneConfirmationEvent(event: PhoneConfirmationEvent) {
        when (event) {
            PhoneConfirmationEvent.BackClicked -> onBackClicked()
            PhoneConfirmationEvent.OtpEntered -> onOtpEntered()
            PhoneConfirmationEvent.RequestNewOtpClicked -> onRequestNewOtpClicked()
        }
    }

    fun onYandexCaptchaEvent(event: YandexCaptchaEvent) {
        when (event) {
            YandexCaptchaEvent.DismissRequested -> {
                _yandexCaptchaState.value = YandexCaptchaState.None
            }

            is YandexCaptchaEvent.TokenReceived -> {
                _yandexCaptchaState.value = YandexCaptchaState.None
                requestNewOtp(event.token)
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneConfirmationScreenAction.BackClicked
            emitSideEffect(PhoneConfirmationSideEffect.Navigate(action))
        }
    }

    private fun onOtpEntered() {
        if (confirmPhoneJob?.isActive == true) return

        confirmPhoneJob = viewModelScope.launch {
            operationTracker.track(Operation.CONFIRM_PHONE) {
                val params = ConfirmSignInUseCase.Params(
                    phone = phone,
                    otp = otpTextFieldState.text.toString(),
                )
                deps.confirmSignIn(params)
                    .onSuccess {
                        val action = PhoneConfirmationScreenAction.PhoneConfirmed
                        emitSideEffect(PhoneConfirmationSideEffect.Navigate(action))
                    }
                    .onFailure(::handlePhoneConfirmationException)
            }
        }
    }

    private fun onRequestNewOtpClicked() {
        if (requestNewOtpJob?.isActive == true) return

        viewModelScope.launch {
            val yandexCaptcha = deps.getYandexCaptcha().getOrNull()
            if (yandexCaptcha != null) {
                _yandexCaptchaState.value = YandexCaptchaState.Started(yandexCaptcha)
            } else {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun requestNewOtp(yandexCaptchaToken: YandexCaptchaToken) {
        if (requestNewOtpJob?.isActive == true) return

        requestNewOtpJob = viewModelScope.launch {
            operationTracker.track(Operation.REQUEST_NEW_OTP) {
                val params = RequestNewAuthOtpUseCase.Params(phone, yandexCaptchaToken)
                deps.requestNewOtp(params)
                    .onSuccess { startNewOtpRequestTimeout() }
                    .onFailure {
                        val text = Text.Resource(RCommon.string.res_new_otp_request_error)
                        showZarinaErrorToast(text)
                    }
            }
        }
    }

    private fun handlePhoneConfirmationException(t: Throwable) {
        if (t is OtpException) {
            isOtpInvalid.value = true
        }

        val messageResId = when (t) {
            is OtpException -> R.string.sign_in_invalid_otp_error
            else -> RCommon.string.res_something_went_wrong
        }
        showZarinaErrorToast(Text.Resource(messageResId))
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

    private fun listenOtpSms() {
        deps.smsCodeRetriever.addListener { otp ->
            otpTextFieldState.setTextAndPlaceCursorAtEnd(otp)
            onOtpEntered()
        }
    }

    private fun makeFieldsValidOnChange() {
        otpTextFieldState.textAsFlow()
            .onEach { isOtpInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PhoneConfirmationSideEffect.ShowZarinaToast(message))
    }

    private enum class Operation : OperationKey { CONFIRM_PHONE, REQUEST_NEW_OTP }

    private companion object {
        private val NEW_OTP_REQUEST_TIMEOUT get() = 1.minutes
    }
}
