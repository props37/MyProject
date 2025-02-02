package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

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
import ru.livetyping.zarina.core.domain.model.user.exception.InvalidOtpException
import ru.livetyping.zarina.core.domain.model.user.exception.OtpException
import ru.livetyping.zarina.core.domain.usecase.user.ConfirmPhoneNumberChangeUseCase
import ru.livetyping.zarina.core.domain.usecase.user.RequestNewPhoneNumberChangeOtpUseCase
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicomponent.otp.OtpStateComponent
import ru.livetyping.zarina.feature.profile.ui.impl.R
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation.model.PhoneChangeConfirmationState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PhoneChangeConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val smsCodeRetriever: SmsCodeRetriever,
    private val confirmPhoneNumberChange: ConfirmPhoneNumberChangeUseCase,
    private val requestNewOtp: RequestNewPhoneNumberChangeOtpUseCase,
) : ViewModel(), SideEffectSource<PhoneChangeConfirmationSideEffect> by SideEffectSourceImpl() {

    private val otpStateComponent = OtpStateComponent(savedStateHandle, viewModelScope)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var confirmPhoneNumberChangeJob: Job? = null
    private var requestNewOtpJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<PhoneChangeConfirmationNavEntry>()
    private val phone = navEntry.getPhone()

    val phoneChangeConfirmationState: StateFlow<PhoneChangeConfirmationState> =
        otpStateComponent.otpState.mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
        ) { otpState ->
            PhoneChangeConfirmationState(
                phone = phone,
                otpState = otpState,
            )
        }

    init {
        listenOtpSms()
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
            try {
                otpStateComponent.setIsOtpLoading(true)
                val params = ConfirmPhoneNumberChangeUseCase.Params(
                    phone = phone,
                    otp = otpStateComponent.otpState.value.otp,
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
            } finally {
                otpStateComponent.setIsOtpLoading(false)
            }
        }
    }

    private fun onPhoneChangeConfirmationFailure(t: Throwable) {
        if (t is OtpException) {
            otpStateComponent.setIsOtpInvalid(true)
        }
        val messageResId = when (t) {
            is InvalidOtpException -> R.string.profile_invalid_otp_error
            else -> RCommon.string.res_something_went_wrong
        }
        showZarinaErrorToast(Text.Resource(messageResId))
    }

    private fun listenOtpSms() {
        smsCodeRetriever.addListener { otp ->
            otpStateComponent.setOtp(otp)
            onOtpEntered()
        }
    }

    private fun requestNewOtp() {
        if (requestNewOtpJob?.isActive == true) return

        requestNewOtpJob = viewModelScope.launch {
            try {
                otpStateComponent.setIsRequestNewOtpButtonLoading(true)
                val params = RequestNewPhoneNumberChangeOtpUseCase.Params(phone)
                requestNewOtp(params)
                    .onSuccess {
                        otpStateComponent.startNewOtpRequestTimeout()
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.profile_new_otp_request_error)
                        showZarinaErrorToast(text)
                    }
            } finally {
                otpStateComponent.setIsRequestNewOtpButtonLoading(false)
            }
        }
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PhoneChangeConfirmationSideEffect.ShowZarinaToast(message))
    }
}
