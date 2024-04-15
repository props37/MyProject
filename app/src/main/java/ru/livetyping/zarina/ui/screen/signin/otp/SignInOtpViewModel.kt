package ru.livetyping.zarina.ui.screen.signin.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.InvalidOtpException
import ru.livetyping.zarina.domain.common.exception.OtpException
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.countdowntimer.CountdownTimer
import ru.livetyping.zarina.ui.common.otp.OtpResendState
import ru.livetyping.zarina.ui.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.ui.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.ui.screen.signin.otp.SignInOtpViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.ConfirmSignInByPhoneUseCase
import ru.livetyping.zarina.usecase.user.RequestResendSmsOtpUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class SignInOtpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SignInOtpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private val countdownTimer = CountdownTimer()

    private var confirmSignInJob: Job? = null
    private var resendOtpJob: Job? = null

    private val otpValueHolder = savedStateHandle.createValueHolder(
        key = KEY_OTP,
        initialValue = "",
    )

    val phone: StateFlow<PhoneNumber> = savedStateHandle
        .getStateFlow<String?>(
            key = SignInGraph.Otp.ARG_KEY_PHONE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { string ->
            checkNotNull(string) { "phone is null" }
            PhoneNumber.create(string)
        }

    val otp: StateFlow<String> = otpValueHolder.stateFlow

    val isOtpLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.CONFIRM_SIGN_UP)
        .stateIn(
            scope = viewModelScope,
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

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignInOtpScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOtpChanged(otp: String) {
        otpValueHolder.set(otp)
        _isOtpInvalid.value = false
    }

    fun onOtpEntered() {
        if (confirmSignInJob?.isActive == true) return

        confirmSignInJob = viewModelScope.launch {
            operationTracker.track(Operation.CONFIRM_SIGN_UP) {
                val params = ConfirmSignInByPhoneUseCase.Params(phone.value, otp.value)
                interactor.confirmSignInByPhone(params)
                    .onSuccess {
                        val action = SignInOtpScreenAction.UserSignedIn
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure(::onOtpFailure)
            }
        }
    }

    fun onResendOtpClicked() {
        if (resendOtpJob?.isActive == true) return
        resendOtpJob = viewModelScope.launch {
            val params = RequestResendSmsOtpUseCase.Params(phone.value)
            interactor.requestResendSmsOtp(params)
                .onSuccess {
                    startOtpResendCountdownTimer()
                }
                .onFailure {
                    val text = Text.Resource(R.string.code_resend_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    private fun onOtpFailure(e: Throwable) {
        if (e is OtpException) {
            _isOtpInvalid.value = true
            otpValueHolder.set("")
        }

        when (e) {
            is InvalidOtpException -> {
                val message = ZarinaToastMessage.error(
                    text = Text.Resource(R.string.sign_up_otp_invalid_otp_error),
                )
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            else -> {
                val message = ZarinaToastMessage.error(
                    text = Text.Resource(R.string.something_went_wrong),
                )
                emitSideEffect(SideEffect.ShowZarinaToast(message))
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignInOtpScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { CONFIRM_SIGN_UP }

    companion object {
        private const val KEY_OTP = "otp"

        private val OTP_RESEND_INITIAL_TIMEOUT: Duration get() = 1.minutes
    }
}
