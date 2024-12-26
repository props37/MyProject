package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
internal class PhoneChangeConfirmationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<PhoneChangeConfirmationSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PhoneChangeConfirmationScreenAction.BackClicked
            emitSideEffect(PhoneChangeConfirmationSideEffect.Navigate(action))
        }
    }
}
