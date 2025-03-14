package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.user.exception.OtpException
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmSignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewSignInByEmailConfirmationOtpUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicomponent.otp.OtpStateComponent
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.model.SignInByEmailConfirmationEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail.model.SignInByEmailConfirmationState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class SignInByEmailConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: SignInByEmailConfirmationDependencies,
) : ViewModel(),
    SideEffectSource<SignInByEmailConfirmationSideEffect> by SideEffectSourceImpl() {

    private val otpStateComponent = OtpStateComponent(savedStateHandle, viewModelScope)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var confirmSignInJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<SignInByEmailConfirmationNavEntry>()
    private val phone = navEntry.getPhone()

    val phoneConfirmationState: StateFlow<SignInByEmailConfirmationState> =
        otpStateComponent.otpState.mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
        ) { otpState ->
            SignInByEmailConfirmationState(
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

    fun onPhoneConfirmationEvent(event: SignInByEmailConfirmationEvent) {
        when (event) {
            SignInByEmailConfirmationEvent.BackClicked -> onBackClicked()
            SignInByEmailConfirmationEvent.OtpEntered -> onOtpEntered()
            SignInByEmailConfirmationEvent.RequestNewOtpClicked -> onRequestNewOtpClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignInByEmailConfirmationScreenAction.BackClicked
            emitSideEffect(SignInByEmailConfirmationSideEffect.Navigate(action))
        }
    }

    private fun onOtpEntered() {
        if (confirmSignInJob?.isActive == true) return

        confirmSignInJob = viewModelScope.launch {
            try {
                otpStateComponent.setIsOtpLoading(true)
                val params = ConfirmSignInByEmailUseCase.Params(
                    phone = phone,
                    otp = otpStateComponent.otpState.value.otp,
                )
                deps.confirmSignInByEmail(params)
                    .onSuccess {
                        val action = SignInByEmailConfirmationScreenAction.SignInConfirmed
                        emitSideEffect(SignInByEmailConfirmationSideEffect.Navigate(action))
                    }
                    .onFailure(::handleSignInConfirmationException)
            } finally {
                otpStateComponent.setIsOtpLoading(false)
            }
        }
    }

    private fun onRequestNewOtpClicked() {
        if (requestNewOtpJob?.isActive == true) return

        requestNewOtpJob = viewModelScope.launch {
            try {
                otpStateComponent.setIsRequestNewOtpButtonLoading(true)
                val params = RequestNewSignInByEmailConfirmationOtpUseCase.Params(phone)
                deps.requestNewSignInByEmailConfirmationOtp(params)
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

    private fun handleSignInConfirmationException(t: Throwable) {
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
        emitSideEffect(SignInByEmailConfirmationSideEffect.ShowZarinaToast(message))
    }
}
