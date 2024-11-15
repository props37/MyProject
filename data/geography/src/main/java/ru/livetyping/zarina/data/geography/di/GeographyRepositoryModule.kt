package ru.livetyping.zarina.data.geography.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.data.geography.impl.GeographyRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GeographyRepositoryModule {

    @Binds
    abstract fun bindGeographyRepository(impl: GeographyRepositoryImpl): GeographyRepository
}
