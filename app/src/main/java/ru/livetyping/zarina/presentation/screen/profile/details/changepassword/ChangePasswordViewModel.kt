package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ChangePasswordInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ChangePasswordScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ChangePasswordScreenAction) : SideEffect
    }
}
