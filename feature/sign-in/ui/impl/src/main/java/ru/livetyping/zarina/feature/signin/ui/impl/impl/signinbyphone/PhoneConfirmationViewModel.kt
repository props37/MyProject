package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.user.exception.OtpException
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewAuthOtpUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicomponent.otp.OtpStateComponent
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaState
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.model.PhoneConfirmationEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyphone.model.PhoneConfirmationState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PhoneConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: PhoneConfirmationDependencies,
) : ViewModel(), SideEffectSource<PhoneConfirmationSideEffect> by SideEffectSourceImpl() {

    private val otpStateComponent = OtpStateComponent(savedStateHandle, viewModelScope)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var confirmPhoneJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<PhoneConfirmationNavEntry>()
    private val phone = navEntry.getPhone()

    private val _yandexCaptchaState = MutableStateFlow<YandexCaptchaState>(YandexCaptchaState.None)
    val yandexCaptchaState: StateFlow<YandexCaptchaState> = _yandexCaptchaState.asStateFlow()

    val phoneConfirmationState: StateFlow<PhoneConfirmationState> =
        otpStateComponent.otpState.mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
        ) { otpState ->
            PhoneConfirmationState(
                phone = phone,
                otpState = otpState,
            )
        }

    init {
        listenOtpSms()
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
        otpStateComponent.setIsRequestNewOtpButtonLoading(false)
        when (event) {
            is YandexCaptchaEvent.DismissRequested -> {
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
            try {
                otpStateComponent.setIsOtpLoading(true)
                val params = ConfirmSignInUseCase.Params(
                    phone = phone,
                    otp = otpStateComponent.otpState.value.otp,
                )
                deps.confirmSignIn(params)
                    .onSuccess {
                        val action = PhoneConfirmationScreenAction.PhoneConfirmed
                        emitSideEffect(PhoneConfirmationSideEffect.Navigate(action))
                    }
                    .onFailure(::handlePhoneConfirmationException)
            } finally {
                otpStateComponent.setIsOtpLoading(false)
            }
        }
    }

    private fun onRequestNewOtpClicked() {
        if (requestNewOtpJob?.isActive == true) return

        viewModelScope.launch {
            val yandexCaptcha = deps.getYandexCaptcha().getOrNull()
            if (yandexCaptcha != null) {
                _yandexCaptchaState.value = YandexCaptchaState.Started(yandexCaptcha)
                otpStateComponent.setIsRequestNewOtpButtonLoading(true)
            } else {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun requestNewOtp(yandexCaptchaToken: YandexCaptchaToken) {
        if (requestNewOtpJob?.isActive == true) return

        requestNewOtpJob = viewModelScope.launch {
            try {
                otpStateComponent.setIsRequestNewOtpButtonLoading(true)
                val params = RequestNewAuthOtpUseCase.Params(phone, yandexCaptchaToken)
                deps.requestNewOtp(params)
                    .onSuccess { otpStateComponent.startNewOtpRequestTimeout() }
                    .onFailure {
                        val text = Text.Resource(RCommon.string.res_new_otp_request_error)
                        showZarinaErrorToast(text)
                    }
            } finally {
                otpStateComponent.setIsRequestNewOtpButtonLoading(false)
            }
        }
    }

    private fun handlePhoneConfirmationException(t: Throwable) {
        if (t is OtpException) {
            otpStateComponent.setIsOtpInvalid(true)
        }

        val messageResId = when (t) {
            is OtpException -> R.string.sign_in_invalid_otp_error
            else -> RCommon.string.res_something_went_wrong
        }
        showZarinaErrorToast(Text.Resource(messageResId))
    }

    private fun listenOtpSms() {
        deps.smsCodeRetriever.addListener { otp ->
            otpStateComponent.setOtp(otp)
            onOtpEntered()
        }
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PhoneConfirmationSideEffect.ShowZarinaToast(message))
    }
}
