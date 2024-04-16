package ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.profile.details.signoutconfirmation.SignOutConfirmationViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SignOutConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: SignOutConfirmationInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onStayClicked() {
        navigationThrottler.throttle {
            val action = SignOutConfirmationScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSignOutClicked() {
        // TODO: [High] Implement
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: SignOutConfirmationScreenAction) : SideEffect
    }
}

