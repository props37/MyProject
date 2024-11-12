package ru.livetyping.zarina.presentation.screen.signup

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.captcha.YandexCaptchaToken
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.common.exception.DateValidationException
import ru.livetyping.zarina.domain.common.exception.EmptyDateException
import ru.livetyping.zarina.domain.common.exception.OtpTimeoutException
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.user.exception.CaptchaException
import ru.livetyping.zarina.domain.user.exception.EmailAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.EmailValidationException
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.domain.user.exception.EmptyPasswordException
import ru.livetyping.zarina.domain.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.FirstNameValidationException
import ru.livetyping.zarina.domain.user.exception.PasswordValidationException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberAlreadyInUseException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberValidationException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.sms.SmsConstants
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.yandexcaptcha.YandexCaptchaDialogState
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.signup.SignUpViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.SignUpUseCase
import ru.livetyping.zarina.usecase.user.ValidateSignUpFieldsUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.kotlin.date.LocalDateUtil
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SignUpInteractor,
) : ViewModel(interactor.smsCodeRetriever), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var signUpJob: Job? = null

    private val firstNameValueHolder = savedStateHandle.createValueHolder(
        key = KEY_FIRST_NAME,
        initialValue = "",
    )

    private val birthDateMillisValueHolder = savedStateHandle.createValueHolder<Long?>(
        key = KEY_BIRTH_DATE_MILLIS,
        initialValue = null,
    )

    private val emailValueHolder = savedStateHandle.createValueHolder(
        key = KEY_EMAIL,
        initialValue = "",
    )

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = PHONE_NUMBER_INITIAL_VALUE,
    )

    private val passwordValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PASSWORD,
        initialValue = "",
    )

    private val receiveEmailsValueHolder = savedStateHandle.createValueHolder(
        key = KEY_RECEIVE_EMAILS,
        initialValue = false,
    )

    private val receiveSmsValueHolder = savedStateHandle.createValueHolder(
        key = KEY_RECEIVE_SMS,
        initialValue = false,
    )

    private val arePoliciesAcceptedValueHolder = savedStateHandle.createValueHolder(
        key = KEY_ARE_POLICIES_ACCEPTED,
        initialValue = false,
    )

    private val isPoliciesErrorVisibleValueHolder = savedStateHandle.createValueHolder(
        key = KEY_IS_POLICIES_ERROR_VISIBLE,
        initialValue = false,
    )

    val firstName: StateFlow<String> = firstNameValueHolder.stateFlow

    private val _isFirstNameInvalid = MutableStateFlow(false)
    val isFirstNameInvalid = _isFirstNameInvalid.asStateFlow()

    val birthDateMillis: StateFlow<Long?> = birthDateMillisValueHolder.stateFlow

    private val _isBirthDateInvalid = MutableStateFlow(false)
    val isBirthDateInvalid = _isBirthDateInvalid.asStateFlow()

    val email: StateFlow<String> = emailValueHolder.stateFlow

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid = _isEmailInvalid.asStateFlow()

    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    private val _isPhoneInvalid = MutableStateFlow(false)
    val isPhoneInvalid = _isPhoneInvalid.asStateFlow()

    val password: StateFlow<String> = passwordValueHolder.stateFlow

    private val _isPasswordInvalid = MutableStateFlow(false)
    val isPasswordInvalid = _isPasswordInvalid.asStateFlow()

    val receiveEmails: StateFlow<Boolean> = receiveEmailsValueHolder.stateFlow

    val receiveSms: StateFlow<Boolean> = receiveSmsValueHolder.stateFlow

    val arePoliciesAccepted: StateFlow<Boolean> = arePoliciesAcceptedValueHolder.stateFlow

    val isPoliciesErrorVisible: StateFlow<Boolean> = isPoliciesErrorVisibleValueHolder.stateFlow

    private val _yandexCaptchaState =
        MutableStateFlow<YandexCaptchaDialogState>(YandexCaptchaDialogState.Hidden)
    val yandexCaptchaState: StateFlow<YandexCaptchaDialogState> = _yandexCaptchaState.asStateFlow()

    val isSignUpButtonLoading: StateFlow<Boolean> = combine(
        operationTracker.ongoingOperationKeys,
        yandexCaptchaState,
    ) { ongoingOperations, yandexCaptchaState ->
        Operation.SIGN_UP in ongoingOperations
                || yandexCaptchaState is YandexCaptchaDialogState.Visible
    }.stateIn(
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
        firstNameValueHolder.set(name)
        _isFirstNameInvalid.value = false
    }

    fun onBirthDateMillisChanged(millis: Long?) {
        birthDateMillisValueHolder.set(millis)
        _isBirthDateInvalid.value = false
    }

    fun onEmailChanged(email: String) {
        emailValueHolder.set(email)
        _isEmailInvalid.value = false
    }

    fun onPhoneChanged(phone: String) {
        val normalizedPhone = PhoneNumberUtils.normalizeNumber(phone)
        phoneValueHolder.set(normalizedPhone)
        _isPhoneInvalid.value = false
    }

    fun onPasswordChanged(password: String) {
        passwordValueHolder.set(password)
        _isPasswordInvalid.value = false
    }

    fun onReceiveEmailsChanged(value: Boolean) {
        receiveEmailsValueHolder.set(value)
    }

    fun onReceiveSmsChanged(value: Boolean) {
        receiveSmsValueHolder.set(value)
    }

    fun onPoliciesAcceptedChanged(areAccepted: Boolean) {
        arePoliciesAcceptedValueHolder.set(areAccepted)
        if (areAccepted) {
            isPoliciesErrorVisibleValueHolder.set(false)
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    fun onSignUpClicked() {
        if (signUpJob?.isActive == true) return

        if (!arePoliciesAccepted.value) {
            isPoliciesErrorVisibleValueHolder.set(true)
            val text = Text.Resource(R.string.sign_up_agreement_error)
            val message = ZarinaToastMessage.error(text)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
            return
        }

        viewModelScope.launch {
            val phone = PhoneNumber.create(phone.value)
            val birthDate = birthDateMillis.value?.let {
                LocalDateUtil.fromMillis(it)
            }
            val email = Email.create(email.value)
            val password = password.value
            val params = ValidateSignUpFieldsUseCase.Params(
                firstName = firstName.value,
                birthDate = birthDate,
                email = email,
                phone = phone,
                password = password,
            )
            interactor.validateSignUpFields(params)
                .onSuccess {
                    val yandexCaptcha = interactor.getYandexCaptcha().getOrNull()
                    if (yandexCaptcha != null) {
                        _yandexCaptchaState.value = YandexCaptchaDialogState.Visible(yandexCaptcha)
                    } else {
                        val messageText = Text.Resource(R.string.something_went_wrong_try_again)
                        val message = ZarinaToastMessage.error(messageText)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure(::onSignUpFailure)
        }
    }

    fun onYandexCaptchaDismissRequested() {
        _yandexCaptchaState.value = YandexCaptchaDialogState.Hidden
    }

    fun onYandexCaptchaTokenReceived(token: YandexCaptchaToken) {
        _yandexCaptchaState.value = YandexCaptchaDialogState.Hidden
        signUp(token)
    }

    private fun signUp(yandexCaptchaToken: YandexCaptchaToken) {
        if (signUpJob?.isActive == true) return

        if (!arePoliciesAccepted.value) {
            isPoliciesErrorVisibleValueHolder.set(true)
            val text = Text.Resource(R.string.sign_up_agreement_error)
            val message = ZarinaToastMessage.error(text)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
            return
        }

        interactor.smsCodeRetriever.start(
            sender = SmsConstants.SENDER_ZARINA,
            codeRegexPattern = SmsConstants.CODE_PATTERN_ZARINA,
        )

        signUpJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_UP) {
                val phone = PhoneNumber.create(phone.value)
                val birthDate = birthDateMillis.value?.let {
                    LocalDateUtil.fromMillis(it)
                }
                val email = Email.create(email.value)
                val password = password.value
                val params = SignUpUseCase.Params(
                    firstName = firstName.value,
                    birthDate = birthDate,
                    email = email,
                    phone = phone,
                    password = password,
                    receiveEmails = receiveEmails.value,
                    receiveSms = receiveSms.value,
                    yandexCaptchaToken = yandexCaptchaToken,
                )
                interactor.signUp(params)
                    .onSuccess {
                        interactor.credentialManager.createCredential(
                            username = email.value,
                            password = password,
                        )

                        val action = SignUpScreenAction.UserCreated(phone)
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure(::onSignUpFailure)
            }
        }
    }

    private fun onSignUpFailure(e: Throwable) {
        when (e) {
            is ValidationException -> handleSignUpValidationException(e)
            is OtpTimeoutException -> {
                val text = Text.Resource(R.string.sign_up_otp_timeout_error)
                val message = ZarinaToastMessage.error(text)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

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
        val isBirthDateEmpty = exceptions.any { it is EmptyDateException }
        val isEmailEmpty = exceptions.any { it is EmptyEmailException }
        val isPhoneEmpty = exceptions.any { it is EmptyPhoneNumberException }
        val isPasswordEmpty = exceptions.any { it is EmptyPasswordException }

        val messageText = when {
            isFirstNameEmpty || isBirthDateEmpty || isEmailEmpty || isPhoneEmpty || isPasswordEmpty -> {
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

        if (exceptions.any { it is FirstNameValidationException }) {
            _isFirstNameInvalid.value = true
        }
        if (exceptions.any { it is DateValidationException }) {
            _isBirthDateInvalid.value = true
        }
        if (exceptions.any { it is EmailValidationException }) {
            _isEmailInvalid.value = true
        }
        if (exceptions.any { it is PhoneNumberValidationException }) {
            _isPhoneInvalid.value = true
        }
        if (exceptions.any { it is PasswordValidationException }) {
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
        private const val KEY_BIRTH_DATE_MILLIS = "birth_date_millis"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_RECEIVE_EMAILS = "receive_emails"
        private const val KEY_RECEIVE_SMS = "receive_sms"
        private const val KEY_ARE_POLICIES_ACCEPTED = "are_policies_accepted"
        private const val KEY_IS_POLICIES_ERROR_VISIBLE = "is_policies_error_visible"

        private const val PHONE_NUMBER_INITIAL_VALUE = "+7"
    }
}
