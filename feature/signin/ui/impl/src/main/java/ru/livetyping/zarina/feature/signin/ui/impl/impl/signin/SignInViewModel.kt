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
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.SignInByEmailParams
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyPasswordException
import ru.livetyping.zarina.core.domain.model.user.exception.PasswordException
import ru.livetyping.zarina.core.domain.validation.SignInValidator
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
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
) : ViewModel(), SideEffectSource<SignInSideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var credentialManagerJob: Job? = null

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

    val signInState: StateFlow<SignInState> = combine(
        isEmailInvalid,
        isPasswordInvalid,
        isPhoneInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isEmailInvalid, isPasswordInvalid, isPhoneInvalid, ongoingOperations ->
        SignInState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            passwordTextFieldState = passwordTextFieldState,
            isPasswordInvalid = isPasswordInvalid,
            phoneTextFieldState = phoneTextFieldState,
            isPhoneInvalid = isPhoneInvalid,
            // TODO: [Top] Depend on yandex captcha state
            isSignInButtonLoading = SignInOperation in ongoingOperations,
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

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignInScreenAction.ScreenClosed
            emitSideEffect(SignInSideEffect.Navigate(action))
        }
    }

    private fun onSignInClicked() {
        when (currentSignInType.value) {
            SignInType.EMAIL -> startSignInByEmail()
            SignInType.PHONE -> TODO()
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

            // TODO: [Top] Start captcha
        } catch (e: Exception) {
            handleSignInException(e)
        }
    }

    private fun handleSignInException(e: Exception) {
        val causes = if (e is CombinedValidationException) e.causes else listOf(e)
        causes.forEach { cause ->
            when (cause) {
                is EmailException -> isEmailInvalid.value = true
                is PasswordException -> isPasswordInvalid.value = true
                // TODO: [Top] Handle phone exception
                // TODO: [Top] Handle other exceptions
            }

            // TODO: [Top] Handle phone exception
            val isEmailEmpty = causes.any { it is EmptyEmailException }
            val isPasswordEmpty = causes.any { it is EmptyPasswordException }
            val messageText = when {
                isEmailEmpty || isPasswordEmpty -> {
                    Text.Resource(R.string.sign_in_by_email_empty_fields_error)
                }

                else -> Text.Resource(RCommon.string.res_incorrect_data_entered)
            }
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

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"
    }
}
