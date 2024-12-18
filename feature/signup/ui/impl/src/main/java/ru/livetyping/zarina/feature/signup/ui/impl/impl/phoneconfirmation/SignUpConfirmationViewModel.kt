package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.exception.OtpException
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignUpUseCase
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
import ru.livetyping.zarina.feature.signup.ui.impl.R
import ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation.model.SignUpConfirmationEvent
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class SignUpConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: SignUpConfirmationDeps,
) : ViewModel(), SideEffectSource<SignUpConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var confirmSignUpJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val countDownTimer = CountDownTimer()

    private val navEntry = savedStateHandle.toRoute<SignUpConfirmationNavEntry>()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val otpTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isOtpInvalid = MutableStateFlow(false)

    private val newOtpRequestState = MutableStateFlow<NewOtpRequestState>(
        NewOtpRequestState.Unavailable(NEW_OTP_REQUEST_TIMEOUT),
    )

    val phone: StateFlow<PhoneNumber> = ReadOnlyStateFlow(PhoneNumber.create(navEntry.phone))

    val otpState: StateFlow<TextFieldOtpState> = combine(
        isOtpInvalid,
        newOtpRequestState,
        operationTracker.ongoingOperationKeys,
    ) { isOtpInvalid, newOtpRequestState, ongoingOperations ->
        TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = Operation.CONFIRM_SIGN_UP in ongoingOperations,
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
        makeFieldsValidOnChange()
    }

    override fun onCleared() {
        deps.smsCodeRetriever.release()
    }

    fun onSignUpConfirmationEvent(event: SignUpConfirmationEvent) {
        when (event) {
            SignUpConfirmationEvent.BackClicked -> onBackClicked()
            SignUpConfirmationEvent.OtpEntered -> onOtpEntered()
            SignUpConfirmationEvent.RequestNewOtpClicked -> onRequestNewOtpClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignUpConfirmationScreenAction.BackClicked
            emitSideEffect(SignUpConfirmationSideEffect.Navigate(action))
        }
    }

    private fun onOtpEntered() {
        if (confirmSignUpJob?.isActive == true) return

        confirmSignUpJob = viewModelScope.launch {
            operationTracker.track(Operation.CONFIRM_SIGN_UP) {
                val params = ConfirmSignUpUseCase.Params(
                    phone = phone.value,
                    otp = otpTextFieldState.text.toString(),
                )
                deps.confirmSignUp(params)
                    .onSuccess {
                        val action = SignUpConfirmationScreenAction.SignUpConfirmed
                        emitSideEffect(SignUpConfirmationSideEffect.Navigate(action))
                    }
                    .onFailure(::handleConfirmSignUpException)
            }
        }
    }

    private fun onRequestNewOtpClicked() {
        if (requestNewOtpJob?.isActive == true) return

        requestNewOtpJob = viewModelScope.launch {
            val params = RequestNewAuthOtpUseCase.Params(phone.value)
            deps.requestNewOtp(params)
                .onSuccess { startNewOtpRequestTimeout() }
                .onFailure {
                    val text = Text.Resource(RCommon.string.res_new_otp_request_error)
                    showZarinaErrorToast(text)
                }
        }
    }

    private fun handleConfirmSignUpException(t: Throwable) {
        if (t is OtpException) {
            isOtpInvalid.value = true
        }

        val messageResId = when (t) {
            is OtpException -> R.string.sign_up_invalid_otp_error
            else -> RCommon.string.res_something_went_wrong
        }
        showZarinaErrorToast(Text.Resource(messageResId))
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
        emitSideEffect(SignUpConfirmationSideEffect.ShowZarinaToast(message))
    }

    private enum class Operation : OperationKey { CONFIRM_SIGN_UP }

    private companion object {
        private val NEW_OTP_REQUEST_TIMEOUT get() = 1.minutes
    }
}
