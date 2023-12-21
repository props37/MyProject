package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.rework.OnboardingStep
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModelRework.SideEffect
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModelRework @Inject constructor(
    private val interactor: OnboardingInteractorRework,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val onboardingSteps = OnboardingStepsBuilder.build(interactor.permissionManager)

    private val currentOnboardingStepIndex = MutableStateFlow(0)

    // TODO: [High] Refactor
    val onboarding = currentOnboardingStepIndex
        .map { currentStepIndex ->
            val coercedStepNumber = currentStepIndex.coerceIn(0, onboardingSteps.lastIndex)
            val currentStep = onboardingSteps[coercedStepNumber]
            val currentPage = Onboarding.Page(
                number = coercedStepNumber + 1,
                step = currentStep,
            )
            Onboarding(
                currentPage = currentPage,
                pageCount = onboardingSteps.size,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Onboarding(
                currentPage = Onboarding.Page(
                    number = 1,
                    step = onboardingSteps.first(),
                ),
                pageCount = onboardingSteps.size,
            ),
        )

    @Immutable
    data class Onboarding(
        val currentPage: Page,
        val pageCount: Int,
    ) {
        @Immutable
        data class Page(
            val number: Int,
            val step: OnboardingStep,
        )
    }

    sealed interface SideEffect: SideEffectSource.SideEffect
}
