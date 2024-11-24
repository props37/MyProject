package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.geo.City

@Immutable
internal data class OnboardingState(
    val onboardingSteps: ImmutableList<OnboardingStep>,
    val currentOnboardingStep: OnboardingStep,
    val city: City,
    val isSkipCityDetectionButtonLoading: Boolean,
    val isDetectCityButtonLoading: Boolean,
    val isConfirmCityButtonLoading: Boolean,
    val bannerUrl: Url?,
) {
    val currentStepIndex: Int by lazy {
        val index = onboardingSteps.indexOf(currentOnboardingStep)
        check(index >= 0) {
            "There is no current onboarding step $currentOnboardingStep in steps $onboardingSteps"
        }
        index
    }
}
