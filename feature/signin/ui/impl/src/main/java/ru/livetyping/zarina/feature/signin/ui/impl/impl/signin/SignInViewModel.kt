package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import androidx.compose.foundation.text.input.TextFieldState
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.credential.CredentialFetchingResult
import ru.livetyping.zarina.core.credential.CredentialManager
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptcha
import ru.livetyping.zarina.core.domain.model.captcha.YandexCaptchaToken
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.SignInByEmailParams
import ru.livetyping.zarina.core.domain.model.user.SignInByPhoneParams
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPhoneException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PhoneException
import ru.livetyping.zarina.core.domain.usecase.user.GetYandexCaptchaUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByEmailUseCase
import ru.livetyping.zarina.core.domain.usecase.user.SignInByPhoneUseCase
import ru.livetyping.zarina.core.domain.validation.SignInValidator
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.YandexCaptchaEvent
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.setTextAndPlaceCursorAtEnd
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uikit.toast.ZarinaToastMessage
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
    private val credentialManager: CredentialManager,
    private val signInByEmail: SignInByEmailUseCase,
    private val signInByPhone: SignInByPhoneUseCase,
    private val getYandexCaptcha: GetYandexCaptchaUseCase,
) : ViewModel(), SideEffectSource<SignInSideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var credentialManagerJob: Job? = null
    private var signInJob: Job? = null

    private val currentSignInType = MutableStateFlow(SignInType.EMAIL)

    val signInTypeSelectorState: StateFlow<TabRowState<SignInType>> = currentSignInType.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
    ) { currentSignInType ->
        TabRowState(
            tabs = SignInType.entries.toImmutableList(),
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

    private val visibleYandexCaptcha = MutableStateFlow<YandexCaptcha?>(null)

    val signInState: StateFlow<SignInState> = combine(
        isEmailInvalid,
        isPasswordInvalid,
        isPhoneInvalid,
        operationTracker.ongoingOperationKeys,
        visibleYandexCaptcha,
    ) { isEmailInvalid, isPasswordInvalid, isPhoneInvalid, ongoingOperations, visibleYandexCaptcha ->
        val isSignInButtonLoading =
            SignInOperation in ongoingOperations || visibleYandexCaptcha != null
        SignInState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            isSignInButtonLoading = isSignInButtonLoading,
            visibleYandexCaptcha = visibleYandexCaptcha,
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
            visibleYandexCaptcha = null,
        )
    )

    private var showSaveCredentialPrompt = true

    private var yandexCaptchaTrigger: YandexCaptchaTrigger? = null

    init {
        makeFieldsValidOnChange()
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
            SignInEvent.ForgotPasswordClicked -> TODO()
            SignInEvent.SignInClicked -> onSignInClicked()
            SignInEvent.SignUpClicked -> TODO()
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
            YandexCaptchaEvent.Dismissed -> visibleYandexCaptcha.value = null
            is YandexCaptchaEvent.TokenReceived -> {
                visibleYandexCaptcha.value = null
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
            val action = SignInScreenAction.ScreenClosed
            emitSideEffect(SignInSideEffect.Navigate(action))
        }
    }

    private fun onSignInClicked() {
        when (currentSignInType.value) {
            SignInType.EMAIL -> startSignInByEmail()
            SignInType.PHONE -> startSignInByPhone()
        }
    }

    private fun onScreenStarted() {
        if (credentialManagerJob?.isActive == true) return
        credentialManagerJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                val result = credentialManager.getCredential()
                if (result is CredentialFetchingResult.Success) {
                    showSaveCredentialPrompt = false
                    emitSideEffect(SignInSideEffect.FreeFocus)
                    emailTextFieldState.edit {
                        setTextAndPlaceCursorAtEnd(result.username)
                    }
                    passwordTextFieldState.edit {
                        setTextAndPlaceCursorAtEnd(result.password)
                    }
                    startSignInByEmail()
                }
            }
        }
    }

    private fun startSignInByEmail() {
        try {
            val signInParams = SignInByEmailParams(
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
        try {
            val signInParams = SignInByPhoneParams(
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
                this@SignInViewModel.signInByEmail(params)
                    .onSuccess {
                        if (showSaveCredentialPrompt) {
                            credentialManager.createCredential(
                                username = email.value,
                                password = password,
                            )
                        }

                        // TODO: [Top] Implement
//                        val action = SignInScreenAction.UserSignedIn
//                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure(::handleSignInException)
            }
        }
    }

    private fun signInByPhone(yandexCaptchaToken: YandexCaptchaToken) {
        if (signInJob?.isActive == true) return

        signInJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                // TODO: [Top] Start SMS code retriever
//                interactor.smsCodeRetriever.start(
//                    sender = SmsConstants.SENDER_ZARINA,
//                    codeRegexPattern = SmsConstants.CODE_PATTERN_ZARINA,
//                )
                val phone = PhoneNumber.create(phoneTextFieldState.text.toString())
                val params = SignInByPhoneUseCase.Params(phone, yandexCaptchaToken)
                this@SignInViewModel.signInByPhone(params)
                    .onSuccess {
                        // TODO: [Top] Implement
//                        val action = SignInScreenAction.SignInByPhoneRequested(phone)
//                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure(::handleSignInException)
            }
        }
    }

    private fun handleSignInException(e: Throwable) {
        val causes = if (e is CombinedValidationException) e.causes else listOf(e)
        causes.forEach { cause ->
            when (cause) {
                is EmailException -> isEmailInvalid.value = true
                is PasswordException -> isPasswordInvalid.value = true
                is PhoneException -> isPhoneInvalid.value = true
                // TODO: [Top] Handle other exceptions
            }

            val isEmailEmpty = causes.any { it is EmptyEmailException }
            val isPasswordEmpty = causes.any { it is EmptyPasswordException }
            val isPhoneEmpty = causes.any { it is EmptyPhoneException }
            val messageTextResId = when {
                isEmailEmpty || isPasswordEmpty -> R.string.sign_in_by_email_empty_fields_error
                isPhoneEmpty -> R.string.sign_in_by_phone_empty_fields_error
                else -> RCommon.string.res_incorrect_data_entered
            }
            val messageText = Text.Resource(messageTextResId)
            val toastMessage = ZarinaToastMessage.error(messageText)
            emitSideEffect(SignInSideEffect.ShowZarinaToast(toastMessage))
        }
    }

    private suspend fun showYandexCaptcha(trigger: YandexCaptchaTrigger) {
        val captcha = getYandexCaptcha().getOrNull()
        if (captcha != null) {
            visibleYandexCaptcha.value = captcha
            yandexCaptchaTrigger = trigger
        } else {
            val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
            val toastMessage = ZarinaToastMessage.error(messageText)
            emitSideEffect(SignInSideEffect.ShowZarinaToast(toastMessage))
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

    private data object SignInOperation : OperationKey

    private enum class YandexCaptchaTrigger {
        SIGN_IN_BY_EMAIL,
        SIGN_IN_BY_PHONE,
    }

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"
    }
}
