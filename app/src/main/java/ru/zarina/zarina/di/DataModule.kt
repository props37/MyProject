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
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.data.device.IDeviceRepository
import ru.zarina.zarina.data.device.local.DeviceLocalSource
import ru.zarina.zarina.data.device.local.IDeviceLocalSource
import ru.zarina.zarina.data.device.remote.DeviceRemoteSource
import ru.zarina.zarina.data.device.remote.IDeviceRemoteSource
import ru.zarina.zarina.data.device.remote.api.DeviceApi
import ru.zarina.zarina.data.device.remote.api.IDeviceApi
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
    fun bindsDeviceApi(api: DeviceApi): IDeviceApi

    @Binds
    fun bindsDeviceRemoteSource(source: DeviceRemoteSource): IDeviceRemoteSource

    @Binds
    fun bindsDeviceLocalSource(source: DeviceLocalSource): IDeviceLocalSource

    @Binds
    fun bindsDeviceRepository(repository: DeviceRepository): IDeviceRepository

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
