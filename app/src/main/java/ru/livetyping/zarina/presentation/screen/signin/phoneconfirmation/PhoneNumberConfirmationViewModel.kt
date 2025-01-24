package ru.livetyping.zarina.presentation.screen.signin.phoneconfirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ru.livetyping.zarina.presentation.navigation.destination.graph.SignInGraph
import ru.livetyping.zarina.presentation.screen.common.otp.OtpViewModelComponent
import ru.livetyping.zarina.presentation.screen.signin.phoneconfirmation.PhoneNumberConfirmationViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.ConfirmPhoneNumberUseCase
import ru.livetyping.zarina.usecase.user.RequestResendPhoneNumberChangeSmsOtpUseCase
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PhoneNumberConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: PhoneNumberConfirmationInteractor,
    private val otpComponent: OtpViewModelComponent,
) : ViewModel(otpComponent, interactor.smsCodeRetriever), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<SignInGraph.PhoneNumberConfirmation>()

    val phone: StateFlow<PhoneNumber> = ImmutableStateFlow(PhoneNumber.create(navEntry.phone))

    val otp: StateFlow<String> = otpComponent.otp

    val isOtpLoading: StateFlow<Boolean> = otpComponent.isOtpLoading

    val isOtpInvalid: StateFlow<Boolean> = otpComponent.isOtpInvalid

    val otpResendState: StateFlow<OtpResendState> = otpComponent.otpResendState

    init {
        interactor.smsCodeRetriever.addListener { code ->
            onOtpChanged(code)
            onOtpEntered()
        }
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneNumberConfirmationScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOtpChanged(otp: String) {
        otpComponent.onOtpChanged(otp)
    }

    fun onOtpEntered() {
        otpComponent.onOtpEntered {
            val params = ConfirmPhoneNumberUseCase.Params(phone.value, otp.value)
            interactor.confirmPhoneNumber(params)
                .onSuccess {
                    val action = PhoneNumberConfirmationScreenAction.PhoneNumberConfirmed
                    emitSideEffect(SideEffect.Navigate(action))
                }
                .onFailure(::onOtpFailure)
        }
    }

    fun onResendOtpClicked() {
        otpComponent.onResendOtpClicked {
            val params = RequestResendPhoneNumberChangeSmsOtpUseCase.Params(phone.value)
            interactor.requestNewOtp(params)
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
        data class Navigate(val action: PhoneNumberConfirmationScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }
}
