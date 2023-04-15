package ru.zarina.zarina.ui.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue

@HiltViewModel
class OnboardingViewModel : ViewModel(),
    ISideEffectSource<OnboardingViewModel.SideEffect> by SideEffectQueue() {

    fun onDetectClick() {}

    fun onSelectClick() {}

    fun onCloseClick() {}

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object ShowHome : SideEffect
    }

}
