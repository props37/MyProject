package ru.livetyping.zarina.ui.screen.signup

import android.telephony.PhoneNumberUtils
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
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.CaptchaException
import ru.livetyping.zarina.domain.user.exception.EmailAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.EmailException
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.domain.user.exception.EmptyPasswordException
import ru.livetyping.zarina.domain.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.FirstNameException
import ru.livetyping.zarina.domain.user.exception.PasswordException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberException
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.ui.screen.signup.SignUpViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.SignUpUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SignUpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var signUpJob: Job? = null

    val firstName: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_FIRST_NAME,
        initialValue = "",
    )

    private val _isFirstNameInvalid = MutableStateFlow(false)
    val isFirstNameInvalid = _isFirstNameInvalid.asStateFlow()

    val email: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_EMAIL,
        initialValue = "",
    )

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid = _isEmailInvalid.asStateFlow()

    val phone: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_PHONE,
        initialValue = "",
    )

    private val _isPhoneInvalid = MutableStateFlow(false)
    val isPhoneInvalid = _isPhoneInvalid.asStateFlow()

    val password: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_PASSWORD,
        initialValue = "",
    )

    private val _isPasswordInvalid = MutableStateFlow(false)
    val isPasswordInvalid = _isPasswordInvalid.asStateFlow()

    val receiveNewsByEmail: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_RECEIVE_NEWS_BE_EMAIL,
        initialValue = false,
    )

    val receiveSmsNotifications: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_RECEIVE_SMS_NOTIFICATIONS,
        initialValue = false,
    )

    val arePoliciesAccepted: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_ARE_POLICIES_ACCEPTED,
        initialValue = false,
    )

    val isPoliciesErrorVisible: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_IS_POLICIES_ERROR_VISIBLE,
        initialValue = false,
    )

    val isSignUpButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.SIGN_UP)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignUpScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onFirstNameChanged(name: String) {
        savedStateHandle[KEY_FIRST_NAME] = name
        _isFirstNameInvalid.value = false
    }

    fun onEmailChanged(email: String) {
        savedStateHandle[KEY_EMAIL] = email
        _isEmailInvalid.value = false
    }

    fun onPhoneChanged(phone: String) {
        val normalizedPhone = PhoneNumberUtils.normalizeNumber(phone)
        savedStateHandle[KEY_PHONE] = normalizedPhone
        _isPhoneInvalid.value = false
    }

    fun onPasswordChanged(password: String) {
        savedStateHandle[KEY_PASSWORD] = password
        _isPasswordInvalid.value = false
    }

    fun onReceiveNewsNyEmailChanged(value: Boolean) {
        savedStateHandle[KEY_RECEIVE_NEWS_BE_EMAIL] = value
    }

    fun onReceiveSmsNotificationsChanged(value: Boolean) {
        savedStateHandle[KEY_RECEIVE_SMS_NOTIFICATIONS] = value
    }

    fun onPoliciesAcceptedChanged(areAccepted: Boolean) {
        savedStateHandle[KEY_ARE_POLICIES_ACCEPTED] = areAccepted
        if (areAccepted) {
            savedStateHandle[KEY_IS_POLICIES_ERROR_VISIBLE] = false
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    fun onSignUpClicked() {
        // TODO: [High] Remove!!!
        val action = SignUpScreenAction.UserCreated(PhoneNumber.create("+78005553535"))
        emitSideEffect(SideEffect.Navigate(action))
        return

        if (signUpJob?.isActive == true) return

        if (!arePoliciesAccepted.value) {
            savedStateHandle[KEY_IS_POLICIES_ERROR_VISIBLE] = true
            val text = Text.Resource(R.string.sign_up_agreement_error)
            val message = ZarinaToastMessage.error(text)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
            return
        }

        signUpJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_UP) {
                val params = SignUpUseCase.Params(
                    firstName = firstName.value,
                    email = Email.create(email.value),
                    phone = PhoneNumber.create(phone.value),
                    password = password.value,
                    receiveNewsByEmail = receiveNewsByEmail.value,
                    receiveSmsNotifications = receiveSmsNotifications.value,
                )
                interactor.signUp(params)
                    .onSuccess {
                        // TODO: [High] Implement
                    }
                    .onFailure(::onSignUpFailure)
            }
        }
    }

    private fun onSignUpFailure(e: Throwable) {
        when (e) {
            is ValidationException -> handleSignUpValidationException(e)
            is CaptchaException -> {
                val text = Text.Resource(R.string.something_went_wrong_try_again)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            else -> {
                val text = Text.Resource(R.string.something_went_wrong)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }
    }

    private fun handleSignUpValidationException(e: ValidationException) {
        val exceptions = listOf(e) + e.suppressedExceptions

        val isFirstNameEmpty = exceptions.any { it is EmptyFirstNameException }
        val isEmailEmpty = exceptions.any { it is EmptyEmailException }
        val isPhoneEmpty = exceptions.any { it is EmptyPhoneNumberException }
        val isPasswordEmpty = exceptions.any { it is EmptyPasswordException }

        val messageText = when {
            isFirstNameEmpty || isEmailEmpty || isPhoneEmpty || isPasswordEmpty -> {
                Text.Resource(R.string.sign_up_empty_fields_error)
            }

            exceptions.any { it is EmailAlreadyInUseException } -> {
                Text.Resource(R.string.sign_up_email_already_in_use_error)
            }

            exceptions.any { it is PhoneNumberAlreadyInUseException } -> {
                Text.Resource(R.string.sign_up_phone_number_already_in_use_error)
            }

            else -> Text.Resource(R.string.incorrect_data_entered)
        }
        val message = ZarinaToastMessage.error(messageText)
        emitSideEffect(SideEffect.ShowZarinaToast(message))

        if (exceptions.any { it is FirstNameException }) {
            _isFirstNameInvalid.value = true
        }
        if (exceptions.any { it is EmailException }) {
            _isEmailInvalid.value = true
        }
        if (exceptions.any { it is PhoneNumberException }) {
            _isPhoneInvalid.value = true
        }
        if (exceptions.any { it is PasswordException }) {
            _isPasswordInvalid.value = true
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignUpScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { SIGN_UP }

    companion object {
        private const val KEY_FIRST_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_RECEIVE_NEWS_BE_EMAIL = "receive_new_by_email"
        private const val KEY_RECEIVE_SMS_NOTIFICATIONS = "receive_sms_notifications"
        private const val KEY_ARE_POLICIES_ACCEPTED = "are_policies_accepted"
        private const val KEY_IS_POLICIES_ERROR_VISIBLE = "is_policies_error_visible"
    }
}
