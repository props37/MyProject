package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.di.key.FeatureEntryKey
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.impl.CatalogFeatureImpl
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.impl.CitySelectorFeatureImpl
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.impl.HomeFeatureImpl
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.impl.OnboardingFeatureImpl
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.impl.WishlistFeatureImpl

@Module
@InstallIn(SingletonComponent::class)
internal class FeatureModule {

    @Provides
    @IntoMap
    @FeatureEntryKey(HomeFeature::class)
    fun provideHomeFeatureEntry(): FeatureEntry<*, *, *> = HomeFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(OnboardingFeature::class)
    fun provideOnboardingFeatureEntry(): FeatureEntry<*, *, *> = OnboardingFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(CatalogFeature::class)
    fun provideCatalogFeatureEntry(): FeatureEntry<*, *, *> = CatalogFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(WishlistFeature::class)
    fun provideWishlistFeatureEntry(): FeatureEntry<*, *, *> = WishlistFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(CitySelectorFeature::class)
    fun provideCitySelectorFeature(): FeatureEntry<*, *, *> = CitySelectorFeatureImpl()
}
