package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.di.key.FeatureEntryKey
import ru.livetyping.zarina.feature.home.ui.HomeFeatureEntry
import ru.livetyping.zarina.feature.home.ui.impl.HomeFeatureEntryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class FeatureModule {

    @Provides
    @Singleton
    @IntoMap
    @FeatureEntryKey(HomeFeatureEntry::class)
    fun provideMainFeatureEntry(): FeatureEntry<*, *> = HomeFeatureEntryImpl()
}
