package ru.livetyping.zarina.feature.onboarding.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface OnboardingFeature :
    ComposableFeatureEntry<OnboardingNavEntry, Unit, OnboardingNavActions> {

    public companion object {
        public fun getNavEntry(): OnboardingNavEntry = OnboardingNavEntry

        public fun getNavEntryClass(): KClass<OnboardingNavEntry> = OnboardingNavEntry::class
    }
}
