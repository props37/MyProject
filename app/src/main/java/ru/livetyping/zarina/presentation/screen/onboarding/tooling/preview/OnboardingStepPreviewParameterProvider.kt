package ru.livetyping.zarina.presentation.screen.onboarding.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.presentation.screen.onboarding.OnboardingViewModel

class OnboardingStepPreviewParameterProvider :
    PreviewParameterProvider<OnboardingViewModel.OnboardingStep> {

    override val values: Sequence<OnboardingViewModel.OnboardingStep>
        get() = OnboardingViewModel.OnboardingStep.entries.asSequence()
}
