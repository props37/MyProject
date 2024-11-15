package ru.livetyping.zarina.feature.catalog.ui.impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import ru.livetyping.zarina.core.difeature.FeatureEntryKey
import ru.livetyping.zarina.core.feature.FeatureEntry
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeatureEntry
import ru.livetyping.zarina.feature.catalog.ui.impl.CatalogFeatureEntryImpl

@Module
@InstallIn(SingletonComponent::class)
internal class CatalogFeatureModule {

    @Provides
    @IntoMap
    @FeatureEntryKey(CatalogFeatureEntry::class)
    fun provideCatalogFeature(): FeatureEntry<*, *> {
        return CatalogFeatureEntryImpl()
    }
}
