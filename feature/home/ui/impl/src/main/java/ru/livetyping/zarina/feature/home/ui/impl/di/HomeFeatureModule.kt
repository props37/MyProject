package ru.livetyping.zarina.feature.home.ui.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.difeature.FeatureEntryKey
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.feature.home.ui.HomeFeatureEntry
import ru.livetyping.zarina.feature.home.ui.impl.HomeFeatureEntryImpl

@Module
@InstallIn(SingletonComponent::class)
internal class HomeFeatureModule {

    @Provides
    @IntoMap
    @FeatureEntryKey(HomeFeatureEntry::class)
    fun provideHomeFeature(): FeatureEntry<*, *> {
        return HomeFeatureEntryImpl()
    }
}