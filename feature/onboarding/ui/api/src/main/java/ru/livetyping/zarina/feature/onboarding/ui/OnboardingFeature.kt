package ru.livetyping.zarina.feature.onboarding.ui

import ru.livetyping.zarina.core.feature.SingleFeatureEntry
import kotlin.reflect.KClass

public interface OnboardingFeature :
    SingleFeatureEntry<OnboardingNavEntry, Unit, OnboardingNavActions> {

    public companion object {
        public fun getNavEntry(): OnboardingNavEntry = OnboardingNavEntry

        public fun getNavEntryClass(): KClass<OnboardingNavEntry> = OnboardingNavEntry::class
    }
}
