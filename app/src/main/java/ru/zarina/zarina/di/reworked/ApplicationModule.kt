package ru.zarina.zarina.di.reworked

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

        @Provides
        @Singleton
        fun provideEncryptedSharedPreferences(
            @ApplicationContext
            context: Context,
        ): SharedPreferences = EncryptedSharedPreferences.create(
            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

        private const val ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "encrypted_shared_preferences"
    }
}
