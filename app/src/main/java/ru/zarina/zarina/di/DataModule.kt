package ru.zarina.zarina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zarina.zarina.data.content.ContentRepository
import ru.zarina.zarina.data.content.IContentRepository
import ru.zarina.zarina.data.content.remote.ContentRemoteSource
import ru.zarina.zarina.data.content.remote.IContentRemoteSource
import ru.zarina.zarina.data.content.remote.api.ContentApi
import ru.zarina.zarina.data.content.remote.api.IContentApi
import ru.zarina.zarina.data.geography.GeographyRepository
import ru.zarina.zarina.data.geography.IGeographyRepository
import ru.zarina.zarina.data.geography.remote.IGeographyRemoteSource
import ru.zarina.zarina.data.geography.remote.KtorGeographyRemoteSource
import ru.zarina.zarina.data.geography.remote.api.GeographyApi
import ru.zarina.zarina.data.geography.remote.api.IGeographyApi
import ru.zarina.zarina.data.location.GeoLocationRepository
import ru.zarina.zarina.data.location.IGeoLocationRepository
import ru.zarina.zarina.data.location.source.IGeoLocationSource
import ru.zarina.zarina.data.location.source.PlayServicesGeoLocationSource

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindGeoLocationSource(source: PlayServicesGeoLocationSource): IGeoLocationSource

    @Binds
    fun bindsGeoLocationRepository(repository: GeoLocationRepository): IGeoLocationRepository

    @Binds
    fun bindsGeographyApi(api: GeographyApi): IGeographyApi

    @Binds
    fun bindsGeographyRemoteSource(source: KtorGeographyRemoteSource): IGeographyRemoteSource

    @Binds
    fun bindsGeographyRepository(repository: GeographyRepository): IGeographyRepository

    @Binds
    fun bindsContentApi(api: ContentApi): IContentApi

    @Binds
    fun bindsContentRemoteSource(source: ContentRemoteSource): IContentRemoteSource

    @Binds
    fun bindsContentRepository(repository: ContentRepository): IContentRepository

}
