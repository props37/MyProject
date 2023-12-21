package ru.zarina.zarina.ui.screens.onboarding.rework

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screens.onboarding.rework.OnboardingViewModelRework.SideEffect
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModelRework @Inject constructor(
    private val interactor: OnboardingInteractorRework,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    sealed interface SideEffect: SideEffectSource.SideEffect
}
