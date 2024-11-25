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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInEvent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInState
import ru.livetyping.zarina.feature.signin.ui.impl.impl.signin.model.SignInType
import javax.inject.Inject

@HiltViewModel
internal class SignInViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
            SignInEvent.SignInClicked -> TODO()
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

    private fun onScreenStarted() {
        if (credentialManagerJob?.isActive == true) return
        credentialManagerJob = viewModelScope.launch {
            operationTracker.track(SignInOperation) {
                // TODO: [Top] Implement
            }
        }
    }

    private data object SignInOperation : OperationKey

    private companion object {
        private const val PHONE_INITIAL_TEXT = "+7"
    }
}
