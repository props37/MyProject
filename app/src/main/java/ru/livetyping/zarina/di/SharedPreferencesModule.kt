package ru.livetyping.zarina.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    @Provides
    @Singleton
    @Qualifiers.SharedPreferences(Qualifiers.ShapredPreferencesType.ENCRYPTED)
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

    companion object {
        private const val ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "encrypted_shared_preferences"
    }
}
