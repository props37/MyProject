package ru.livetyping.zarina.ui.screen.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.screen.signin.SignInViewModel.SideEffect
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val interactor: SignInInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect
}
