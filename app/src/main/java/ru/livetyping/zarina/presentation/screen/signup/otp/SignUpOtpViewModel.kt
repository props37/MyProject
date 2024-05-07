package ru.livetyping.zarina.presentation.screen.signup.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.exception.InvalidOtpException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.otp.OtpResendState
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.presentation.screen.common.otp.OtpViewModelComponent
import ru.livetyping.zarina.presentation.screen.signup.otp.SignUpOtpViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.ConfirmSignUpUseCase
import ru.livetyping.zarina.usecase.user.RequestResendSmsOtpUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class SignUpOtpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SignUpOtpInteractor,
    private val otpComponent: OtpViewModelComponent,
) : ViewModel(otpComponent), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val phone: StateFlow<PhoneNumber> = savedStateHandle
        .getStateFlow<String?>(
            key = SignUpGraph.Otp.ARG_KEY_PHONE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { string ->
            checkNotNull(string) { "phone is null" }
            PhoneNumber.create(string)
        }

    val otp: StateFlow<String> = otpComponent.otp

    val isOtpLoading: StateFlow<Boolean> = otpComponent.isOtpLoading

    val isOtpInvalid: StateFlow<Boolean> = otpComponent.isOtpInvalid

    val otpResendState: StateFlow<OtpResendState> = otpComponent.otpResendState

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignUpOtpScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOtpChanged(otp: String) {
        otpComponent.onOtpChanged(otp)
    }

    fun onOtpEntered() {
        otpComponent.onOtpEntered {
            val params = ConfirmSignUpUseCase.Params(phone.value, otp.value)
            interactor.confirmSignUp(params)
                .onSuccess {
                    val action = SignUpOtpScreenAction.SignUpConfirmed
                    emitSideEffect(SideEffect.Navigate(action))
                }
                .onFailure(::onOtpFailure)
        }
    }

    fun onResendOtpClicked() {
        otpComponent.onResendOtpClicked {
            val params = RequestResendSmsOtpUseCase.Params(phone.value)
            interactor.requestResendSmsOtp(params)
                .onFailure {
                    val text = Text.Resource(R.string.code_resend_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    private fun onOtpFailure(e: Throwable) {
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignUpOtpScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }
}
