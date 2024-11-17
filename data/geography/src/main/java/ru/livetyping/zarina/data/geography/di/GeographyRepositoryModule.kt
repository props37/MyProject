package ru.livetyping.zarina.data.geography.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.data.geography.impl.GeographyRepositoryImpl
import ru.livetyping.zarina.data.geography.impl.local.CityDataHolder
import ru.livetyping.zarina.data.geography.impl.local.CityDataHolderImpl
import ru.livetyping.zarina.data.geography.impl.local.GeographyLocalDataSource
import ru.livetyping.zarina.data.geography.impl.local.GeographyLocalDataSourceImpl
import ru.livetyping.zarina.data.geography.impl.remote.GeographyRemoteDataSource
import ru.livetyping.zarina.data.geography.impl.remote.GeographyRemoteDataSourceImpl
import ru.livetyping.zarina.data.geography.impl.remote.api.GeographyApi
import ru.livetyping.zarina.data.geography.impl.remote.api.GeographyApiImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GeographyRepositoryModule {

    @Binds
    abstract fun bindGeographyRepository(impl: GeographyRepositoryImpl): GeographyRepository

    @Binds
    abstract fun bindGeographyRemoteDataSource(
        impl: GeographyRemoteDataSourceImpl,
    ): GeographyRemoteDataSource

    @Binds
    abstract fun bindGeographyApi(impl: GeographyApiImpl): GeographyApi

    @Binds
    abstract fun bindGeographyLocalDataSource(
        impl: GeographyLocalDataSourceImpl,
    ): GeographyLocalDataSource

    @Binds
    abstract fun bindCityDataHolder(
        impl: CityDataHolderImpl,
    ): CityDataHolder
}
