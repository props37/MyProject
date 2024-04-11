package ru.livetyping.zarina.ui.screen.signin.passwordrecovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.ui.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.signin.passwordrecovery.PasswordRecoveryViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: PasswordRecoveryInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val operationTracker = OperationTracker()

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val emailValueHolder = savedStateHandle.createValueHolder(
        key = KEY_EMAIL,
        initialValue = "",
    )

    val email: StateFlow<String> = emailValueHolder.stateFlow

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid: StateFlow<Boolean> = _isEmailInvalid.asStateFlow()

    val isSendButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.SEND)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PasswordRecoveryScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onEmailChanged(email: String) {
        emailValueHolder.set(email)
    }

    fun onSendClicked() {
        // TODO: [High] Implement
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: PasswordRecoveryScreenAction) : SideEffect
    }

    private enum class Operation : OperationKey { SEND }

    companion object {
        private const val KEY_EMAIL = "email"
    }
}
