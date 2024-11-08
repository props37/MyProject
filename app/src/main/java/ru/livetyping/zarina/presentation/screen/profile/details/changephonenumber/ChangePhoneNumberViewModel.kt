package ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber

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
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.InvalidPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberValidationException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.sms.SmsConstants
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.profile.details.changephonenumber.ChangePhoneNumberViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.ChangePhoneNumberUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class ChangePhoneNumberViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ChangePhoneNumberInteractor,
) : ViewModel(interactor.smsCodeRetriever), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var changePhoneJob: Job? = null

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = PHONE_INITIAL_VALUE
    )

    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    private val _isPhoneInvalid = MutableStateFlow(false)
    val isPhoneInvalid = _isPhoneInvalid.asStateFlow()

    val isChangePhoneButtonLoading = operationTracker
        .isOperationOngoing(Operation.CHANGE_PHONE)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ChangePhoneNumberScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onPhoneChanged(phone: String) {
        phoneValueHolder.set(phone)
        _isPhoneInvalid.value = false
    }

    fun onPhoneEntered() {
        onChangePhoneClicked()
    }

    fun onChangePhoneClicked() {
        if (changePhoneJob?.isActive == true) return

        interactor.smsCodeRetriever.start(
            sender = SmsConstants.SENDER_ZARINA,
            codeRegexPattern = SmsConstants.CODE_PATTERN_ZARINA,
        )

        changePhoneJob = viewModelScope.launch {
            operationTracker.track(Operation.CHANGE_PHONE) {
                val phone = PhoneNumber.create(phone.value)
                val params = ChangePhoneNumberUseCase.Params(phone)
                interactor.changePhoneNumber(params)
                    .onSuccess { onChangePhoneSuccess(phone) }
                    .onFailure(::onChangePhoneFailure)
            }
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    private fun onChangePhoneSuccess(phone: PhoneNumber) {
        val action = ChangePhoneNumberScreenAction.PhoneChangeRequested(phone)
        emitSideEffect(SideEffect.Navigate(action))
    }

    private fun onChangePhoneFailure(throwable: Throwable) {
        when (throwable) {
            is ValidationException -> {
                val exceptions = listOf(throwable) + throwable.suppressedExceptions
                val text = when {
                    exceptions.any { it is PhoneNumberAlreadyInUseException } -> {
                        Text.Resource(R.string.sign_up_phone_number_already_in_use_error)
                    }

                    exceptions.any { it is InvalidPhoneNumberException } -> {
                        Text.Resource(R.string.enter_valid_phone_number)
                    }

                    else -> {
                        Text.Resource(R.string.something_went_wrong)
                    }
                }
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))

                if (exceptions.any { it is PhoneNumberValidationException }) {
                    _isPhoneInvalid.value = true
                }
            }

            else -> {
                val text = Text.Resource(R.string.something_went_wrong)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ChangePhoneNumberScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { CHANGE_PHONE }

    companion object {
        private const val PHONE_INITIAL_VALUE = "+7"

        private const val KEY_PHONE = "phone"
    }
}
