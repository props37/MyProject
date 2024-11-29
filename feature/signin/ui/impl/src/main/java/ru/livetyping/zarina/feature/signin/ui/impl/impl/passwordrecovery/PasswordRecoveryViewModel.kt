package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.model.PasswordRecoveryState
import javax.inject.Inject

@HiltViewModel
internal class PasswordRecoveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<PasswordRecoverySideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    val passwordRecoveryState: StateFlow<PasswordRecoveryState> = combine(
        isEmailInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isEmailInvalid, ongoingOperations ->
        PasswordRecoveryState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            isSendButtonLoading = RequestPasswordRecoveryOperation in ongoingOperations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PasswordRecoveryState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid.value,
            isSendButtonLoading = false,
        )
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PasswordRecoveryScreenAction.BackClicked
            emitSideEffect(PasswordRecoverySideEffect.Navigate(action))
        }
    }

    fun onRequestPasswordRecoveryClicked() {
        // TODO: [Top] Implement
    }

    private data object RequestPasswordRecoveryOperation : OperationKey
}
