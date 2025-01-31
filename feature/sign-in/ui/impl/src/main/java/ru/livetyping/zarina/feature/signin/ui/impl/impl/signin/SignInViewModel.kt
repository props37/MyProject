package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.credential.CredentialFetchingResult
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.sms.ZarinaSms
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.UserNotFoundException
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.core.domain.validation.SignInValidator
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaEvent
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaState
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class SignInViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: SignInDependencies,
) : ViewModel(), SideEffectSource<SignInSideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var credentialManagerJob: Job? = null
    private var signInJob: Job? = null

    private val signInTypes = SignInType.getAll().toImmutableList()
    private val currentSignInType = MutableStateFlow(SignInType.EMAIL)

    val signInTypeSelectorState: StateFlow<TabRowState<SignInType>> = currentSignInType.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
    ) { currentSignInType ->
        TabRowState(
            tabs = signInTypes,
            currentTab = currentSignInType,
        )
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val passwordTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isPasswordInvalid = MutableStateFlow(false)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val phoneTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_TEXT) },
    )

    private val isPhoneInvalid = MutableStateFlow(false)

    private val _yandexCaptchaState = MutableStateFlow<YandexCaptchaState>(YandexCaptchaState.None)
    val yandexCaptchaState: StateFlow<YandexCaptchaState> = _yandexCaptchaState.asStateFlow()

    val signInState: StateFlow<SignInState> = combine(
        isEmailInvalid,
        isPasswordInvalid,
        isPhoneInvalid,
        operationTracker.ongoingOperationKeys,
        yandexCaptchaState,
    ) { isEmailInvalid, isPasswordInvalid, isPhoneInvalid, ongoingOperations, yandexCaptchaState ->
        val isSignInButtonLoading = SignInOperation in ongoingOperations
                || yandexCaptchaState is YandexCaptchaState.Started

        SignInState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            isSignInButtonLoading = isSignInButtonLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SignInState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid.value,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid.value,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid.value,
            isSignInButtonLoading = false,
        )
    )

    private var showSaveCredentialPrompt = true

    private var yandexCaptchaTrigger: YandexCaptchaTrigger? = null

    init {
        makeFieldsValidOnChange()
    }

    override fun onCleared() {
        deps.smsCodeRetriever.stop()
    }

    fun onSignInTypeSelectorEvent(event: TabRowEvent<SignInType>) {
        when (event) {
            is TabRowEvent.TabChanged -> currentSignInType.value = event.tab
            is TabRowEvent.TabReselected -> Unit
        }
    }

    fun onSignInEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.BackClicked -> onBackClicked()
            SignInEvent.ForgotPasswordClicked -> onForgotPasswordClicked()
            SignInEvent.SignInClicked -> onSignInClicked()
            SignInEvent.SignUpClicked -> onSignUpClicked()
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> Unit
            LifecycleEvent.ON_START -> onScreenStarted()
            LifecycleEvent.ON_RESUME -> Unit
        }
    }

    fun onYandexCaptchaEvent(event: YandexCaptchaEvent) {
        when (event) {
            is YandexCaptchaEvent.DismissRequested -> {
                _yandexCaptchaState.value = YandexCaptchaState.None
            }

            is YandexCaptchaEvent.TokenReceived -> {
                _yandexCaptchaState.value = YandexCaptchaState.None
                when (yandexCaptchaTrigger) {
                    YandexCaptchaTrigger.SIGN_IN_BY_EMAIL -> signInByEmail(event.token)
                    YandexCaptchaTrigger.SIGN_IN_BY_PHONE -> signInByPhone(event.token)
                    null -> Unit
                }
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.BackClicked
            emitSideEffect(SignInSideEffect.Navigate(action))
        }
    }

    private fun onForgotPasswordClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.ForgotPasswordClicked
            emitSideEffect(SignInSideEffect.Navigate(action))
        }
    }

    private fun onSignInClicked() {
        when (currentSignInType.value) {
            SignInType.EMAIL -> startSignInByEmail()
            SignInType.PHONE -> startSignInByPhone()
        }
    }

    private fun onSignUpClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.SignUpClicked
            emitSideEffect(SignInSideEffect.Navigate(action))
        }
    }

    private fun onScreenStarted() {
        if (credentialManagerJob?.isActive == true) return
        credentialManagerJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                val result = deps.credentialManager.getCredential()
                if (result is CredentialFetchingResult.Success) {
                    showSaveCredentialPrompt = false
                    emitSideEffect(SignInSideEffect.FreeFocus)
                    emailTextFieldState.setTextAndPlaceCursorAtEnd(result.username)
                    passwordTextFieldState.setTextAndPlaceCursorAtEnd(result.password)
                    startSignInByEmail()
                }
            }
        }
    }

    private fun startSignInByEmail() {
        if (signInJob?.isActive == true) return

        try {
            val signInParams = SignInValidator.SignInByEmailParams(
                email = Email.create(emailTextFieldState.text.toString()),
                password = passwordTextFieldState.text.toString(),
            )
            val validator = SignInValidator()
            validator.validate(signInParams)

            viewModelScope.launch {
                showYandexCaptcha(YandexCaptchaTrigger.SIGN_IN_BY_EMAIL)
            }
        } catch (e: Exception) {
            handleSignInException(e)
        }
    }

    private fun startSignInByPhone() {
        if (signInJob?.isActive == true) return

        try {
            val signInParams = SignInValidator.SignInByPhoneParams(
                phone = PhoneNumber.create(phoneTextFieldState.text.toString())
            )
            val validator = SignInValidator()
            validator.validate(signInParams)

            viewModelScope.launch {
                showYandexCaptcha(YandexCaptchaTrigger.SIGN_IN_BY_PHONE)
            }
        } catch (e: Exception) {
            handleSignInException(e)
        }
    }

    private fun signInByEmail(yandexCaptchaToken: YandexCaptchaToken) {
        if (signInJob?.isActive == true) return

        signInJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                val email = Email.create(emailTextFieldState.text.toString())
                val password = passwordTextFieldState.text.toString()
                val params = SignInByEmailUseCase.Params(email, password, yandexCaptchaToken)
                deps.signInByEmail(params)
                    .onSuccess {
                        if (showSaveCredentialPrompt) {
                            deps.credentialManager.createCredential(
                                username = email.value,
                                password = password,
                            )
                        }

                        val action = SignInScreenAction.UserSignedIn
                        emitSideEffect(SignInSideEffect.Navigate(action))
                    }
                    .onFailure(::handleSignInException)
            }
        }
    }

    private fun signInByPhone(yandexCaptchaToken: YandexCaptchaToken) {
        if (signInJob?.isActive == true) return

        signInJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                deps.smsCodeRetriever.start(
                    sender = ZarinaSms.SENDER,
                    codeRegexPattern = ZarinaSms.CODE_REGEX_PATTERN_ZARINA,
                )
                val phone = PhoneNumber.create(phoneTextFieldState.text.toString())
                val params = SignInByPhoneUseCase.Params(phone, yandexCaptchaToken)
                deps.signInByPhone(params)
                    .onSuccess {
                        val action = SignInScreenAction.SignInByPhoneRequested(phone)
                        emitSideEffect(SignInSideEffect.Navigate(action))
                    }
                    .onFailure(::handleSignInException)
            }
        }
    }

    private fun handleSignInException(t: Throwable) {
        when (t) {
            is CombinedValidationException -> handleSignInCombinedValidationException(t)
            is EmailException -> {
                isEmailInvalid.value = true
                val textResId = when (t) {
                    is EmptyEmailException -> R.string.sign_in_by_email_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is PasswordException -> {
                isPasswordInvalid.value = true
                val textResId = when (t) {
                    is EmptyPasswordException -> R.string.sign_in_by_email_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is PhoneNumberException -> {
                isPhoneInvalid.value = true
                val textResId = when (t) {
                    is EmptyPhoneNumberException -> R.string.sign_in_by_phone_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is UserNotFoundException -> {
                val text = Text.Resource(R.string.sign_in_invalid_email_or_password_try_again)
                showZarinaErrorToast(text)
            }

            else -> {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun handleSignInCombinedValidationException(e: CombinedValidationException) {
        val causes = e.causes
        causes.forEach { cause ->
            when (cause) {
                is EmailException -> isEmailInvalid.value = true
                is PasswordException -> isPasswordInvalid.value = true
                is PhoneNumberException -> isPhoneInvalid.value = true
            }
        }

        val isEmailEmpty = causes.any { it is EmptyEmailException }
        val isPasswordEmpty = causes.any { it is EmptyPasswordException }
        val isPhoneEmpty = causes.any { it is EmptyPhoneNumberException }

        val messageTextResId = when {
            isEmailEmpty || isPasswordEmpty -> R.string.sign_in_by_email_empty_fields_error
            isPhoneEmpty -> R.string.sign_in_by_phone_empty_fields_error
            else -> RCommon.string.res_incorrect_data_entered
        }
        val messageText = Text.Resource(messageTextResId)
        showZarinaErrorToast(messageText)
    }

    private suspend fun showYandexCaptcha(trigger: YandexCaptchaTrigger) {
        val captcha = deps.getYandexCaptcha().getOrNull()
        if (captcha != null) {
            _yandexCaptchaState.value = YandexCaptchaState.Started(captcha)
            yandexCaptchaTrigger = trigger
        } else {
            val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
            showZarinaErrorToast(messageText)
        }
    }

    private fun makeFieldsValidOnChange() {
        emailTextFieldState.textAsFlow()
            .onEach { isEmailInvalid.value = false }
            .launchIn(viewModelScope)
        passwordTextFieldState.textAsFlow()
            .onEach { isPasswordInvalid.value = false }
            .launchIn(viewModelScope)
        phoneTextFieldState.textAsFlow()
            .onEach { isPhoneInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val toastMessage = ZarinaToastMessage.error(text)
        emitSideEffect(SignInSideEffect.ShowZarinaToast(toastMessage))
    }

    private data object SignInOperation : OperationKey

    private enum class YandexCaptchaTrigger {
        SIGN_IN_BY_EMAIL,
        SIGN_IN_BY_PHONE,
    }

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"
    }
}
