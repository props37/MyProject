package ru.livetyping.zarina.presentation.screen.profile.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.profile.details.ProfileDetailsViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProfileDetailsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onSignOutClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.SignOutClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeleteAccountClicked() {
        navigationThrottler.throttle {
            val action = ProfileDetailsScreenAction.DeleteAccountClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProfileDetailsScreenAction) : SideEffect
    }
}
