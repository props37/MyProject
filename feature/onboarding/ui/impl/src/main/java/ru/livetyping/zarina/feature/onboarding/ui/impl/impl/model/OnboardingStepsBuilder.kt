package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model

import javax.inject.Inject

// TODO: [Low] Refactor
internal class OnboardingStepsBuilder @Inject constructor() {
    fun build(isNotificationsPermissionGranted: Boolean): List<OnboardingStep> = buildList {
        OnboardingStep.entries.forEach { step ->
            when (step) {
                OnboardingStep.NOTIFICATIONS_SETUP -> {
                    if (!isNotificationsPermissionGranted) {
                        add(step)
                    }
                }

                else -> add(step)
            }
        }
    }
}
