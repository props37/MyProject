package ru.livetyping.zarina.data.location.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.data.location.GooglePlayServicesLocationDataSource
import ru.livetyping.zarina.data.location.LocationDataSource
import ru.livetyping.zarina.data.location.LocationRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationRepositoryModule {

    @Binds
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindLocationDataSource(
        impl: GooglePlayServicesLocationDataSource,
    ): LocationDataSource
}
