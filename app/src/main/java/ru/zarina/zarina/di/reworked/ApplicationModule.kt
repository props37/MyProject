package ru.zarina.zarina.di.reworked

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import ru.zarina.zarina.data.permissionmanager.PermissionManager
import ru.zarina.zarina.data.permissionmanager.PermissionManagerImpl
import ru.zarina.zarina.data.rework.location.GooglePlayServicesLocationDataSource
import ru.zarina.zarina.data.rework.location.LocationDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindPermissionManager(
        permissionManagerImpl: PermissionManagerImpl,
    ): PermissionManager

    @Binds
    abstract fun bindLocationDataSource(
        googlePlayServicesLocationDataSource: GooglePlayServicesLocationDataSource,
    ): LocationDataSource

    companion object {

        @OptIn(ExperimentalSerializationApi::class)
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }
    }
}
