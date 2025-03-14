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
import ru.livetyping.zarina.core.coroutinesutil.combineMore
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.credential.CredentialFetchingResult
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.sms.ZarinaSms
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneNumberException
import ru.livetyping.zarina.core.domain.model.user.exception.UserNotFoundException
import ru.livetyping.zarina.core.domain.usecase.user.RequestSignInPhoneConfirmationUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.core.domain.validation.PhoneValidator
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
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaReason
import ru.livetyping.zarina.core.uikit.captcha.YandexCaptchaState
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.R
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInByEmailState
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
    private var requestPhoneConfirmationJob: Job? = null

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

    private val signInByEmailStep = MutableStateFlow(SignInByEmailStep.MAIN)

    @OptIn(SavedStateHandleSaveableApi::class)
    private val phoneToConfirmTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState(PHONE_INITIAL_TEXT) },
    )

    private val isPhoneToConfirmInvalid = MutableStateFlow(false)

    private val _yandexCaptchaState = MutableStateFlow<YandexCaptchaState>(YandexCaptchaState.None)
    val yandexCaptchaState: StateFlow<YandexCaptchaState> = _yandexCaptchaState.asStateFlow()

    private val signInByEmailState = combineMore(
        signInByEmailStep,
        isEmailInvalid,
        isPasswordInvalid,
        isPhoneToConfirmInvalid,
        operationTracker.ongoingOperationKeys,
        yandexCaptchaState,
    ) { step, isEmailInvalid, isPasswordInvalid, isPhoneToConfirmInvalid, ongoingOperations, yandexCaptchaState ->
        when (step) {
            SignInByEmailStep.MAIN -> {
                SignInByEmailState.Main(
                    emailTextFieldState = emailTextFieldState,
                    isEmailInvalid = isEmailInvalid,
                    passwordTextFieldState = passwordTextFieldState,
                    isPasswordInvalid = isPasswordInvalid,
                )
            }

            SignInByEmailStep.PHONE_CONFIRMATION -> {
                val isCaptchaActive = yandexCaptchaState is YandexCaptchaState.Started
                        && yandexCaptchaState.reason == CaptchaReason.PHONE_CONFIRMATION
                val isGetConfirmationCodeButtonLoading =
                    RequestPhoneConfirmationOperation in ongoingOperations || isCaptchaActive
                SignInByEmailState.PhoneConfirmation(
                    phoneTextFieldState = phoneToConfirmTextFieldState,
                    isPhoneInvalid = isPhoneToConfirmInvalid,
                    isGetConfirmationCodeButtonLoading = isGetConfirmationCodeButtonLoading,
                )
            }
        }
    }

    private val initialSignInByEmailState = SignInByEmailState.Main(
        emailTextFieldState = emailTextFieldState,
        isEmailInvalid = isEmailInvalid.value,
        passwordTextFieldState = passwordTextFieldState,
        isPasswordInvalid = isPasswordInvalid.value,
    )

    val signInState: StateFlow<SignInState> = combine(
        signInByEmailState,
        isPhoneInvalid,
        operationTracker.ongoingOperationKeys,
        yandexCaptchaState,
    ) { signInByEmailState, isPhoneInvalid, ongoingOperations, yandexCaptchaState ->
        val isCaptchaActive = yandexCaptchaState is YandexCaptchaState.Started
                && (yandexCaptchaState.reason == CaptchaReason.SIGN_IN_BY_EMAIL
                || yandexCaptchaState.reason == CaptchaReason.SIGN_IN_BY_PHONE)
        val isSignInButtonLoading = SignInOperation in ongoingOperations
                || RequestPhoneConfirmationOperation in ongoingOperations
                || isCaptchaActive

        SignInState(
            signInByEmailState = signInByEmailState,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            isSignInButtonLoading = isSignInButtonLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = SignInState(
            signInByEmailState = initialSignInByEmailState,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid.value,
            isSignInButtonLoading = false,
        )
    )

    private var showSaveCredentialPrompt = true

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
            SignInEvent.GetPhoneConfirmationCodeClicked -> onGetPhoneConfirmationCodeClicked()
        }
    }

    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event) {
            LifecycleEvent.ON_CREATE -> onScreenCreated()
            LifecycleEvent.ON_START -> Unit
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
                when (event.reason) {
                    CaptchaReason.SIGN_IN_BY_EMAIL -> signInByEmail(event.token)
                    CaptchaReason.SIGN_IN_BY_PHONE -> signInByPhone(event.token)
                    CaptchaReason.PHONE_CONFIRMATION -> requestPhoneConfirmation(event.token)
                    null -> Unit
                }
            }
        }
    }

    private fun onBackClicked() {
        if (signInByEmailStep.value == SignInByEmailStep.PHONE_CONFIRMATION) {
            requestPhoneConfirmationJob?.cancel()
            val captchaState = yandexCaptchaState.value
            if (
                captchaState is YandexCaptchaState.Started
                && captchaState.reason == CaptchaReason.PHONE_CONFIRMATION
            ) {
                _yandexCaptchaState.value = YandexCaptchaState.None
            }
            signInByEmailStep.value = SignInByEmailStep.MAIN
            phoneToConfirmTextFieldState.setTextAndPlaceCursorAtEnd(PHONE_INITIAL_TEXT)
        } else {
            navigationThrottler.throttle {
                val action = SignInScreenAction.BackClicked
                emitSideEffect(SignInSideEffect.Navigate(action))
            }
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

    private fun onGetPhoneConfirmationCodeClicked() {
        startPhoneConfirmationRequest()
    }

    private fun onScreenCreated() {
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
        if (isSignInInProgress()) return

        try {
            val signInParams = SignInValidator.SignInByEmailParams(
                email = Email.create(emailTextFieldState.text.toString()),
                password = passwordTextFieldState.text.toString(),
            )
            val validator = SignInValidator()
            validator.validate(signInParams)

            viewModelScope.launch {
                showYandexCaptcha(CaptchaReason.SIGN_IN_BY_EMAIL)
            }
        } catch (e: Exception) {
            handleSignInException(e)
        }
    }

    private fun startSignInByPhone() {
        if (isSignInInProgress()) return

        try {
            val signInParams = SignInValidator.SignInByPhoneParams(
                phone = PhoneNumber.create(phoneTextFieldState.text.toString())
            )
            val validator = SignInValidator()
            validator.validate(signInParams)

            viewModelScope.launch {
                showYandexCaptcha(CaptchaReason.SIGN_IN_BY_PHONE)
            }
        } catch (e: Exception) {
            handleSignInException(e)
        }
    }

    private fun startPhoneConfirmationRequest() {
        if (requestPhoneConfirmationJob?.isActive == true) return

        try {
            val phone = PhoneNumber.create(phoneToConfirmTextFieldState.text.toString())
            val phoneValidator = PhoneValidator()
            phoneValidator.validate(phone)
            requestPhoneConfirmationJob = viewModelScope.launch {
                showYandexCaptcha(CaptchaReason.PHONE_CONFIRMATION)
            }
        } catch (e: PhoneNumberException) {
            isPhoneToConfirmInvalid.value = true
            val text = Text.Resource(R.string.sign_in_enter_valid_phone_number)
            showZarinaErrorToast(text)
        }
    }

    private fun signInByEmail(yandexCaptchaToken: YandexCaptchaToken) {
        if (isSignInInProgress()) return

        signInJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                val email = Email.create(emailTextFieldState.text.toString())
                val password = passwordTextFieldState.text.toString()
                val params = SignInByEmailUseCase.Params(email, password, yandexCaptchaToken)
                deps.signInByEmail(params)
                    .onSuccess { authResult ->
                        handleSignInByEmailSuccess(email, password, authResult)
                    }
                    .onFailure(::handleSignInException)
            }
        }
    }

    private fun signInByPhone(yandexCaptchaToken: YandexCaptchaToken) {
        if (isSignInInProgress()) return

        signInJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                deps.smsCodeRetriever.start(
                    sender = ZarinaSms.SENDER,
                    codeRegexPattern = ZarinaSms.CODE_REGEX_PATTERN,
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

    private fun requestPhoneConfirmation(yandexCaptchaToken: YandexCaptchaToken) {
        if (requestPhoneConfirmationJob?.isActive == true) return

        deps.smsCodeRetriever.start(
            sender = ZarinaSms.SENDER,
            codeRegexPattern = ZarinaSms.CODE_REGEX_PATTERN,
        )

        requestPhoneConfirmationJob = viewModelScope.launch {
            operationTracker.track(RequestPhoneConfirmationOperation) {
                val phone = PhoneNumber.create(phoneToConfirmTextFieldState.text.toString())
                val params = RequestSignInPhoneConfirmationUseCase.Params(phone, yandexCaptchaToken)
                deps.requestSignInPhoneConfirmation(params)
                    .onSuccess {
                        signInByEmailStep.value = SignInByEmailStep.MAIN
                        val action = SignInScreenAction.PhoneConfirmationNeeded(phone)
                        emitSideEffect(SignInSideEffect.Navigate(action))
                    }
                    .onFailure {
                        val text = Text.Resource(R.string.sign_in_phone_confirmation_error)
                        showZarinaErrorToast(text)
                    }
            }
        }
    }

    private suspend fun handleSignInByEmailSuccess(
        email: Email,
        password: String,
        authResult: AuthResult,
    ) {
        if (showSaveCredentialPrompt) {
            deps.credentialManager.createCredential(
                username = email.value,
                password = password,
            )
        }

        val phoneToConfirm = authResult.phoneConfirmation?.phone
        when {
            !authResult.isPhoneConfirmationNeeded() -> {
                val action = SignInScreenAction.UserSignedIn
                emitSideEffect(SignInSideEffect.Navigate(action))
            }

            authResult.isPhoneConfirmationNeeded() && phoneToConfirm != null -> {
                phoneToConfirmTextFieldState.setTextAndPlaceCursorAtEnd(phoneToConfirm.value)
                startPhoneConfirmationRequest()
            }

            else -> {
                phoneToConfirmTextFieldState.setTextAndPlaceCursorAtEnd(PHONE_INITIAL_TEXT)
                signInByEmailStep.value = SignInByEmailStep.PHONE_CONFIRMATION
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

    private suspend fun showYandexCaptcha(reason: CaptchaReason) {
        val captcha = deps.getYandexCaptcha().getOrNull()
        if (captcha != null) {
            _yandexCaptchaState.value = YandexCaptchaState.Started(captcha, reason)
        } else {
            val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
            showZarinaErrorToast(messageText)
        }
    }

    private fun isSignInInProgress(): Boolean {
        return signInJob?.isActive == true || requestPhoneConfirmationJob?.isActive == true
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
        phoneToConfirmTextFieldState.textAsFlow()
            .onEach { isPhoneToConfirmInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val toastMessage = ZarinaToastMessage.error(text)
        emitSideEffect(SignInSideEffect.ShowZarinaToast(toastMessage))
    }

    enum class SignInByEmailStep { MAIN, PHONE_CONFIRMATION }

    private data object SignInOperation : OperationKey

    private data object RequestPhoneConfirmationOperation : OperationKey

    private enum class CaptchaReason : YandexCaptchaReason {
        SIGN_IN_BY_EMAIL,
        SIGN_IN_BY_PHONE,
        PHONE_CONFIRMATION,
    }

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"
    }
}
