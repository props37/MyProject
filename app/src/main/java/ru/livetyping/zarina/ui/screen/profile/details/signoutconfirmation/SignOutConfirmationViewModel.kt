package ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation

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
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation.SignOutConfirmationViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class SignOutConfirmationViewModel @Inject constructor(
    private val interactor: SignOutConfirmationInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var signOutJob: Job? = null

    val isSignOutButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.SIGN_OUT)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onStayClicked() {
        navigationThrottler.throttle {
            val action = SignOutConfirmationScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSignOutClicked() {
        if (signOutJob?.isActive == true) return
        signOutJob = viewModelScope.launch {
            operationTracker.track(Operation.SIGN_OUT) {
                interactor.signOut()
                    .onSuccess {
                        val action = SignOutConfirmationScreenAction.UserSignedOut
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure {
                        val message = Text.Resource(R.string.sign_out_error)
                        emitSideEffect(SideEffect.ShowToast(message))
                    }
            }
        }
    }

    private enum class Operation : OperationKey { SIGN_OUT }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignOutConfirmationScreenAction) : SideEffect

        data class ShowToast(val message: Text) : SideEffect
    }
}

