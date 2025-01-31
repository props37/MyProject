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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidOtpException
import ru.livetyping.zarina.core.domain.model.user.exception.OtpException
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmPhoneNumberChangeUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewPhoneNumberChangeOtpUseCase
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
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationState
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [High] Refactor. Follow DRY. Extract common code and reuse it.
@HiltViewModel
internal class PhoneChangeConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val smsCodeRetriever: SmsCodeRetriever,
    private val confirmPhoneNumberChange: ConfirmPhoneNumberChangeUseCase,
    private val requestNewOtp: RequestNewPhoneNumberChangeOtpUseCase,
) : ViewModel(), SideEffectSource<PhoneChangeConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var confirmPhoneNumberChangeJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val countDownTimer = CountDownTimer()

    private val navEntry = savedStateHandle.toRoute<PhoneChangeConfirmationNavEntry>()
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
            isLoading = Operation.CONFIRM_PHONE_CHANGE in ongoingOperations,
            isInvalid = isOtpInvalid,
            newOtpRequestState = newOtpRequestState,
            isRequestNewOtpButtonLoading = Operation.REQUEST_NEW_OTP in ongoingOperations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TextFieldOtpState(
            textFieldState = otpTextFieldState,
            isLoading = false,
            isInvalid = false,
            newOtpRequestState = NewOtpRequestState.Available,
            isRequestNewOtpButtonLoading = false,
        ),
    )

    val phoneChangeConfirmationState: StateFlow<PhoneChangeConfirmationState> = otpState.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { otpState ->
        PhoneChangeConfirmationState(
            phone = phone,
            otpState = otpState,
        )
    }

    init {
        startNewOtpRequestTimeout()
        listenOtpSms()
        makeFieldsValidOnChange()
    }

    override fun onCleared() {
        smsCodeRetriever.stop()
    }

    fun onPhoneChangeConfirmationEvent(event: PhoneChangeConfirmationEvent) {
        when (event) {
            PhoneChangeConfirmationEvent.BackClicked -> onBackClicked()
            PhoneChangeConfirmationEvent.OtpEntered -> onOtpEntered()
            PhoneChangeConfirmationEvent.RequestNewOtpClicked -> requestNewOtp()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneChangeConfirmationScreenAction.BackClicked
            emitSideEffect(PhoneChangeConfirmationSideEffect.Navigate(action))
        }
    }

    private fun onOtpEntered() {
        if (confirmPhoneNumberChangeJob?.isActive == true) return

        viewModelScope.launch {
            operationTracker.track(Operation.CONFIRM_PHONE_CHANGE) {
                val params = ConfirmPhoneNumberChangeUseCase.Params(
                    phone = phone,
                    otp = otpTextFieldState.text.toString(),
                )
                confirmPhoneNumberChange(params)
                    .onSuccess {
                        val text = Text.Resource(R.string.profile_phone_number_changed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(PhoneChangeConfirmationSideEffect.ShowZarinaToast(message))

                        val action = PhoneChangeConfirmationScreenAction.PhoneChangeConfirmed
                        emitSideEffect(PhoneChangeConfirmationSideEffect.Navigate(action))
                    }
                    .onFailure(::onPhoneChangeConfirmationFailure)
            }
        }
    }

    private fun onPhoneChangeConfirmationFailure(t: Throwable) {
        if (t is OtpException) isOtpInvalid.value = true
        val messageResId = when (t) {
            is InvalidOtpException -> R.string.profile_invalid_otp_error
            else -> RCommon.string.res_something_went_wrong
        }
        showZarinaErrorToast(Text.Resource(messageResId))
    }

    private fun listenOtpSms() {
        smsCodeRetriever.addListener { otp ->
            otpTextFieldState.setTextAndPlaceCursorAtEnd(otp)
            onOtpEntered()
        }
    }

    private fun requestNewOtp() {
        if (requestNewOtpJob?.isActive == true) return

        requestNewOtpJob = viewModelScope.launch {
            operationTracker.track(Operation.REQUEST_NEW_OTP) {
                val params = RequestNewPhoneNumberChangeOtpUseCase.Params(phone)
                requestNewOtp(params)
                    .onSuccess {
                        startNewOtpRequestTimeout()
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.profile_new_otp_request_error)
                        showZarinaErrorToast(text)
                    }
            }
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
