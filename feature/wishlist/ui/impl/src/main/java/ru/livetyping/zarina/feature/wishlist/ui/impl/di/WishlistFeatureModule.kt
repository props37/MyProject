package ru.livetyping.zarina.feature.wishlist.ui.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.difeature.FeatureEntryKey
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeatureEntry
import ru.livetyping.zarina.feature.wishlist.ui.impl.WishlistFeatureEntryImpl

@Module
@InstallIn(SingletonComponent::class)
internal class WishlistFeatureModule {

    @Provides
    @IntoMap
    @FeatureEntryKey(WishlistFeatureEntry::class)
    fun provideWishlistFeature(): FeatureEntry<*, *> {
        return WishlistFeatureEntryImpl()
    }
}
