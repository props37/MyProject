package ru.livetyping.zarina.presentation.screen.profile.details.accountdeletionconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.profile.details.accountdeletionconfirmation.AccountDeletionConfirmationViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class AccountDeletionConfirmationViewModel @Inject constructor(
    private val interactor: AccountDeletionConfirmationInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var deleteAccountJob: Job? = null

    val isDeleteButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.DELETE_ACCOUNT)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onKeepClicked() {
        navigationThrottler.throttle {
            val action = AccountDeletionConfirmationScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeleteClicked() {
        if (deleteAccountJob?.isActive == true) return
        deleteAccountJob = viewModelScope.launch {
            operationTracker.track(Operation.DELETE_ACCOUNT) {
                interactor.deleteAccount()
                    .onSuccess {
                        val action = AccountDeletionConfirmationScreenAction.AccountDeleted
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure {
                        val message = Text.Resource(R.string.account_deletion_error)
                        emitSideEffect(SideEffect.ShowToast(message))
                    }
            }
        }
    }

    private enum class Operation : OperationKey { DELETE_ACCOUNT }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: AccountDeletionConfirmationScreenAction) : SideEffect

        data class ShowToast(val message: Text) : SideEffect
    }
}
