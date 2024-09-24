package ru.livetyping.zarina.presentation.screen.signin

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
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
import ru.livetyping.zarina.domain.user.exception.EmailException
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.EmptyPasswordException
import ru.livetyping.zarina.domain.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.domain.user.exception.PasswordException
import ru.livetyping.zarina.domain.user.exception.PhoneNumberException
import ru.livetyping.zarina.domain.user.exception.UserNotFoundException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.credentialmanager.CredentialFetchingResult
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.sms.SmsConstants
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.screen.signin.SignInViewModel.SideEffect
import ru.livetyping.zarina.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SignInInteractor,
) : ViewModel(interactor.smsCodeRetriever), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var signInJob: Job? = null

    private val currentSignInTypeValueHolder = savedStateHandle.createValueHolder(
        key = KEY_CURRENT_SIGN_IN_TYPE,
        initialValue = SignInType.EMAIL,
    )

    private val emailValueHolder = savedStateHandle.createValueHolder(
        key = KEY_EMAIL,
        initialValue = "",
    )

    private val passwordValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PASSWORD,
        initialValue = "",
    )

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = PHONE_NUMBER_INITIAL_VALUE,
    )

    val signInTypes: StateFlow<ImmutableList<SignInType>> =
        ImmutableStateFlow(SignInType.entries.toImmutableList())

    val currentSignInType: StateFlow<SignInType> = currentSignInTypeValueHolder.stateFlow

    val email: StateFlow<String> = emailValueHolder.stateFlow

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid: StateFlow<Boolean> = _isEmailInvalid.asStateFlow()

    val password: StateFlow<String> = passwordValueHolder.stateFlow

    private val _isPasswordInvalid = MutableStateFlow(false)
    val isPasswordInvalid: StateFlow<Boolean> = _isPasswordInvalid.asStateFlow()

    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    private val _isPhoneInvalid = MutableStateFlow(false)
    val isPhoneInvalid: StateFlow<Boolean> = _isPhoneInvalid.asStateFlow()

    private var showSaveCredentialPrompt = true

    val isSignInButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.SIGN_IN)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onScreenOpened() {
        if (signInJob?.isActive == true) return
        signInJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_IN) {
                val result = interactor.credentialManager.getCredential()
                if (result is CredentialFetchingResult.Success) {
                    showSaveCredentialPrompt = false
                    emitSideEffect(SideEffect.FreeFocus)
                    emailValueHolder.set(result.username)
                    passwordValueHolder.set(result.password)
                    signInByEmail()
                }
            }
        }
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSignInTypeChanged(type: SignInType) {
        currentSignInTypeValueHolder.set(type)
    }

    fun onEmailChanged(email: String) {
        emailValueHolder.set(email)
        _isEmailInvalid.value = false
    }

    fun onPasswordChanged(password: String) {
        passwordValueHolder.set(password)
        _isPasswordInvalid.value = false
    }

    fun onPhoneChanged(phone: String) {
        phoneValueHolder.set(phone)
        _isPhoneInvalid.value = false
    }

    fun onSignInClicked() {
        if (signInJob?.isActive == true) return
        signInJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_IN) {
                when (currentSignInType.value) {
                    SignInType.EMAIL -> signInByEmail()
                    SignInType.PHONE -> signInByPhone()
                }
            }
        }
    }

    fun onForgotPasswordClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.ForgotPasswordClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSignUpClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.SignUpClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    private suspend fun signInByEmail() {
        val email = Email.create(email.value)
        val password = password.value
        val params = SignInByEmailUseCase.Params(email, password)
        interactor.signInByEmail(params)
            .onSuccess {
                if (showSaveCredentialPrompt) {
                    interactor.credentialManager.createCredential(
                        username = email.value,
                        password = password,
                    )
                }

                val action = SignInScreenAction.UserSignedIn
                emitSideEffect(SideEffect.Navigate(action))
            }
            .onFailure(::onSignInByEmailFailure)
    }

    private suspend fun signInByPhone() {
        interactor.smsCodeRetriever.start(
            sender = SmsConstants.SENDER_ZARINA,
            codeRegexPattern = SmsConstants.CODE_PATTERN_ZARINA,
        )
        val phone = PhoneNumber.create(phone.value)
        val params = SignInByPhoneUseCase.Params(phone)
        interactor.signInByPhone(params)
            .onSuccess {
                val action = SignInScreenAction.SignInByPhoneRequested(phone)
                emitSideEffect(SideEffect.Navigate(action))
            }
            .onFailure(::onSignInByPhoneFailure)
    }

    private fun onSignInByEmailFailure(e: Throwable) {
        when (e) {
            is ValidationException -> handleSignInByEmailValidationException(e)
            is UserNotFoundException -> {
                val text = Text.Resource(R.string.invalid_email_or_password_try_again)
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

    private fun handleSignInByEmailValidationException(e: ValidationException) {
        val exceptions = listOf(e) + e.suppressedExceptions

        val isEmailEmpty = exceptions.any { it is EmptyEmailException }
        val isPasswordEmpty = exceptions.any { it is EmptyPasswordException }

        val messageText = when {
            isEmailEmpty || isPasswordEmpty -> {
                Text.Resource(R.string.sign_in_by_email_empty_fields_error)
            }

            else -> Text.Resource(R.string.incorrect_data_entered)
        }
        val message = ZarinaToastMessage.error(messageText)
        emitSideEffect(SideEffect.ShowZarinaToast(message))

        if (exceptions.any { it is EmailException }) {
            _isEmailInvalid.value = true
        }
        if (exceptions.any { it is PasswordException }) {
            _isPasswordInvalid.value = true
        }
    }

    private fun onSignInByPhoneFailure(e: Throwable) {
        when (e) {
            is ValidationException -> handleSignInByPhoneValidationException(e)
            is UserNotFoundException -> {
                val text = Text.Resource(R.string.user_with_this_phone_number_not_found_error)
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

    private fun handleSignInByPhoneValidationException(e: ValidationException) {
        val exceptions = listOf(e) + e.suppressedExceptions

        val messageText = when {
            exceptions.any { it is EmptyPhoneNumberException } -> {
                Text.Resource(R.string.sign_in_by_phone_empty_fields_error)
            }

            else -> Text.Resource(R.string.incorrect_data_entered)
        }
        val message = ZarinaToastMessage.error(messageText)
        emitSideEffect(SideEffect.ShowZarinaToast(message))

        if (exceptions.any { it is PhoneNumberException }) {
            _isPhoneInvalid.value = true
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignInScreenAction) : SideEffect

        data object FreeFocus : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Parcelize
    enum class SignInType : Parcelable { EMAIL, PHONE }

    private enum class Operation : OperationKey { SIGN_IN }

    companion object {
        private const val KEY_CURRENT_SIGN_IN_TYPE = "current_sign_in_type"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PHONE = "phone"

        private const val PHONE_NUMBER_INITIAL_VALUE = "+7"
    }
}
