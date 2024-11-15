package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.di.key.FeatureEntryKey
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeatureEntry
import ru.livetyping.zarina.feature.catalog.ui.impl.CatalogFeatureEntryImpl
import ru.livetyping.zarina.feature.home.ui.HomeFeatureEntry
import ru.livetyping.zarina.feature.home.ui.impl.HomeFeatureEntryImpl
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeatureEntry
import ru.livetyping.zarina.feature.onboarding.ui.impl.OnboardingFeatureEntryImpl
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeatureEntry
import ru.livetyping.zarina.feature.wishlist.ui.impl.WishlistFeatureEntryImpl

@Module
@InstallIn(SingletonComponent::class)
internal class FeatureModule {

    @Provides
    @IntoMap
    @FeatureEntryKey(HomeFeatureEntry::class)
    fun provideHomeFeatureEntry(): FeatureEntry<*, *> = HomeFeatureEntryImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(OnboardingFeatureEntry::class)
    fun provideOnboardingFeatureEntry(): FeatureEntry<*, *> = OnboardingFeatureEntryImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(CatalogFeatureEntry::class)
    fun provideCatalogFeatureEntry(): FeatureEntry<*, *> = CatalogFeatureEntryImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(WishlistFeatureEntry::class)
    fun provideWishlistFeatureEntry(): FeatureEntry<*, *> = WishlistFeatureEntryImpl()
}
