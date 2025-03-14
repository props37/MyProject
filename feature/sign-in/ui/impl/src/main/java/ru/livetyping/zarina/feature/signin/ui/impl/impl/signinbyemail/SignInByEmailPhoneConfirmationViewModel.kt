package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
internal class SignInByEmailPhoneConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    SideEffectSource<SignInByEmailPhoneConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SignInByEmailPhoneConfirmationScreenAction.BackClicked
            emitSideEffect(SignInByEmailPhoneConfirmationSideEffect.Navigate(action))
        }
    }
}
