package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.model.EmailChangingEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging.model.EmailChangingState
import javax.inject.Inject

@HiltViewModel
internal class EmailChangingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<EmailChangingSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var changeEmailJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isEmailInvalid = MutableStateFlow(false)

    val emailChangingState: StateFlow<EmailChangingState> = combine(
        isEmailInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isEmailInvalid, ongoingOperations ->
        val isChangeEmailButtonLoading = ChangeEmailOperation in ongoingOperations
        EmailChangingState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            isChangeEmailButtonLoading = isChangeEmailButtonLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = EmailChangingState(
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = false,
            isChangeEmailButtonLoading = false,
        ),
    )

    init {
        makeFieldValidOnChange()
    }

    fun onEmailChangingEvent(event: EmailChangingEvent) {
        when (event) {
            EmailChangingEvent.BackClicked -> onBackClicked()
            EmailChangingEvent.ChangeEmailClicked -> onChangeEmailClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = EmailChangingScreenAction.BackClicked
            emitSideEffect(EmailChangingSideEffect.Navigate(action))
        }
    }

    private fun onChangeEmailClicked() {
        if (changeEmailJob?.isActive == true) return

        changeEmailJob = viewModelScope.launch {
            operationTracker.track(ChangeEmailOperation) {
                TODO()
                // TODO: [Top] Implement
            }
        }
    }

    private fun makeFieldValidOnChange() {
        emailTextFieldState.textAsFlow()
            .onEach { isEmailInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private data object ChangeEmailOperation : OperationKey
}
