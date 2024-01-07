package ru.zarina.zarina.ui.screen.onboarding.tooling.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel

class OnboardingStepPreviewParameterProvider :
    PreviewParameterProvider<OnboardingViewModel.OnboardingStep> {

    override val values: Sequence<OnboardingViewModel.OnboardingStep>
        get() = OnboardingViewModel.OnboardingStep.entries.asSequence()
}
