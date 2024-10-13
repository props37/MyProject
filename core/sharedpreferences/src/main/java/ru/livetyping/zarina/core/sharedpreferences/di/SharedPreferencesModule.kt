package ru.livetyping.zarina.core.sharedpreferences.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.sharedpreferences.impl.getEncryptedSharedPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SharedPreferencesModule {

    @Provides
    @Singleton
    @SharedPreferencesQualifier(SharedPreferencesType.ENCRYPTED)
    fun provideEncryptedSharedPreferences(
        @ApplicationContext
        context: Context,
    ): SharedPreferences {
        return getEncryptedSharedPreferences(context)
    }
}
