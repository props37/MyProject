package ru.zarina.zarina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.zarina.zarina.data.content.ContentRepository
import ru.zarina.zarina.data.content.IContentRepository
import ru.zarina.zarina.data.content.remote.IContentRemoteSource
import ru.zarina.zarina.data.content.remote.ZarinaContentRemoteSource
import ru.zarina.zarina.data.content.remote.api.IZarinaContentApi
import ru.zarina.zarina.data.content.remote.api.KtorZarinaContentApi
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.data.device.IDeviceRepository
import ru.zarina.zarina.data.device.local.DeviceLocalSource
import ru.zarina.zarina.data.device.local.IDeviceLocalSource
import ru.zarina.zarina.data.device.remote.IDeviceRemoteSource
import ru.zarina.zarina.data.device.remote.ZarinaDeviceRemoteSource
import ru.zarina.zarina.data.device.remote.api.IZarinaDeviceApi
import ru.zarina.zarina.data.device.remote.api.KtorZarinaDeviceApi
import ru.zarina.zarina.data.geography.GeographyRepository
import ru.zarina.zarina.data.geography.IGeographyRepository
import ru.zarina.zarina.data.geography.local.IGeographyLocalSource
import ru.zarina.zarina.data.geography.local.MemoryGeographyLocalSource
import ru.zarina.zarina.data.geography.remote.IGeographyRemoteSource
import ru.zarina.zarina.data.geography.remote.ZarinaGeographyRemoteSource
import ru.zarina.zarina.data.geography.remote.api.IZarinaGeographyApi
import ru.zarina.zarina.data.geography.remote.api.KtorZarinaGeographyApi
import ru.zarina.zarina.data.location.GeoLocationRepository
import ru.zarina.zarina.data.location.IGeoLocationRepository
import ru.zarina.zarina.data.location.source.IGeoLocationSource
import ru.zarina.zarina.data.location.source.PlayServicesGeoLocationSource
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.data.product.ProductRepository
import ru.zarina.zarina.data.product.remote.IProductRemoteSource
import ru.zarina.zarina.data.product.remote.ZarinaProductRemoteSource
import ru.zarina.zarina.data.product.remote.api.IZarinaProductApi
import ru.zarina.zarina.data.product.remote.api.KtorZarinaProductApi
import ru.zarina.zarina.data.recommendation.IRecommendationRepository
import ru.zarina.zarina.data.recommendation.RecommendationRepository
import ru.zarina.zarina.data.recommendation.remote.IRecommendationRemoteSource
import ru.zarina.zarina.data.recommendation.remote.MindboxRecommendationRemoteSource
import ru.zarina.zarina.data.recommendation.remote.api.IMindboxRecommendationApi
import ru.zarina.zarina.data.recommendation.remote.api.KtorMindboxRecommendationApi
import ru.zarina.zarina.data.user.IUserRepository
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.data.user.remote.IUserRemoteSource
import ru.zarina.zarina.data.user.remote.ZarinaUserRemoteSource
import ru.zarina.zarina.data.user.remote.api.IZarinaUserApi
import ru.zarina.zarina.data.user.remote.api.KtorZarinaUserApi

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsZarinaDeviceApi(api: KtorZarinaDeviceApi): IZarinaDeviceApi

    @Binds
    fun bindsDeviceRemoteSource(source: ZarinaDeviceRemoteSource): IDeviceRemoteSource

    @Binds
    fun bindsDeviceLocalSource(source: DeviceLocalSource): IDeviceLocalSource

    @Binds
    fun bindsDeviceRepository(repository: DeviceRepository): IDeviceRepository

    @Binds
    fun bindGeoLocationSource(source: PlayServicesGeoLocationSource): IGeoLocationSource

    @Binds
    fun bindsGeoLocationRepository(repository: GeoLocationRepository): IGeoLocationRepository

    @Binds
    fun bindsZarinaGeographyApi(api: KtorZarinaGeographyApi): IZarinaGeographyApi

    @Binds
    fun bindsGeographyLocalSource(source: MemoryGeographyLocalSource): IGeographyLocalSource

    @Binds
    fun bindsGeographyRemoteSource(source: ZarinaGeographyRemoteSource): IGeographyRemoteSource

    @Binds
    fun bindsGeographyRepository(repository: GeographyRepository): IGeographyRepository

    @Binds
    fun bindsZarinaContentApi(api: KtorZarinaContentApi): IZarinaContentApi

    @Binds
    fun bindsContentRemoteSource(source: ZarinaContentRemoteSource): IContentRemoteSource

    @Binds
    fun bindsContentRepository(repository: ContentRepository): IContentRepository

    @Binds
    fun bindsKtorZarinaUserApi(api: KtorZarinaUserApi): IZarinaUserApi

    @Binds
    fun bindsUserRemoteSource(source: ZarinaUserRemoteSource): IUserRemoteSource

    @Binds
    fun bindsUserRepository(repository: UserRepository): IUserRepository

    @Binds
    fun bindZarinaProductApi(api: KtorZarinaProductApi): IZarinaProductApi

    @Binds
    fun bindsProductRemoteSource(source: ZarinaProductRemoteSource): IProductRemoteSource

    @Binds
    fun bindsProductRepository(repository: ProductRepository): IProductRepository

    @Binds
    fun bindsMindboxRecommendationApi(api: KtorMindboxRecommendationApi): IMindboxRecommendationApi

    @Binds
    fun bindsRecommendationRemoteSource(source: MindboxRecommendationRemoteSource): IRecommendationRemoteSource

    @Binds
    fun bindsRecommendationRepository(repository: RecommendationRepository): IRecommendationRepository

}
