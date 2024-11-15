package ru.livetyping.zarina.feature.onboarding.ui.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.difeature.FeatureEntryKey
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeatureEntry
import ru.livetyping.zarina.feature.onboarding.ui.impl.OnboardingFeatureEntryImpl

@Module
@InstallIn(SingletonComponent::class)
internal class OnboardingFeatureModule {

    @Provides
    @IntoMap
    @FeatureEntryKey(OnboardingFeatureEntry::class)
    fun provideOnboardingFeature(): FeatureEntry<*, *> {
        return OnboardingFeatureEntryImpl()
    }
}
