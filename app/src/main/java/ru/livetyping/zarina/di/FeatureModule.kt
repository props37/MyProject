package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.di.key.FeatureEntryKey
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature
import ru.livetyping.zarina.feature.cart.ui.impl.CartFeatureImpl
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature
import ru.livetyping.zarina.feature.catalog.ui.impl.CatalogFeatureImpl
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature
import ru.livetyping.zarina.feature.cityselector.ui.impl.CitySelectorFeatureImpl
import ru.livetyping.zarina.feature.home.ui.HomeFeature
import ru.livetyping.zarina.feature.home.ui.impl.HomeFeatureImpl
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature
import ru.livetyping.zarina.feature.onboarding.ui.impl.OnboardingFeatureImpl
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature
import ru.livetyping.zarina.feature.productlist.ui.impl.ProductListFeatureImpl
import ru.livetyping.zarina.feature.profile.ui.ProfileFeature
import ru.livetyping.zarina.feature.profile.ui.impl.ProfileFeatureImpl
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature
import ru.livetyping.zarina.feature.signin.ui.impl.SignInFeatureImpl
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature
import ru.livetyping.zarina.feature.signup.ui.impl.SignUpFeatureImpl
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature
import ru.livetyping.zarina.feature.wishlist.ui.impl.WishlistFeatureImpl

@Module
@InstallIn(SingletonComponent::class)
internal class FeatureModule {

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
    @FeatureEntryKey(HomeFeature::class)
    fun provideHomeFeatureEntry(): FeatureEntry<*, *, *> = HomeFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(ProfileFeature::class)
    fun provideProfileFeature(): FeatureEntry<*, *, *> = ProfileFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(CartFeature::class)
    fun provideCartFeature(): FeatureEntry<*, *, *> = CartFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(OnboardingFeature::class)
    fun provideOnboardingFeatureEntry(): FeatureEntry<*, *, *> = OnboardingFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(CitySelectorFeature::class)
    fun provideCitySelectorFeature(): FeatureEntry<*, *, *> = CitySelectorFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(SignInFeature::class)
    fun provideSignInFeature(): FeatureEntry<*, *, *> = SignInFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(SignUpFeature::class)
    fun provideSignUpFeature(): FeatureEntry<*, *, *> = SignUpFeatureImpl()

    @Provides
    @IntoMap
    @FeatureEntryKey(ProductListFeature::class)
    fun provideProductListFeature(): FeatureEntry<*, *, *> = ProductListFeatureImpl()
}
