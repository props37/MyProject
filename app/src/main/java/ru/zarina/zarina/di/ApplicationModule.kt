package ru.zarina.zarina.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import ru.zarina.zarina.data.location.GooglePlayServicesLocationDataSource
import ru.zarina.zarina.data.location.LocationDataSource
import ru.zarina.zarina.ui.common.permissionmanager.PermissionManager
import ru.zarina.zarina.ui.common.permissionmanager.PermissionManagerImpl
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
