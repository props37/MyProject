package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.DigineticaApiKey

@Module
@InstallIn(SingletonComponent::class)
class DigineticaApiKeyModule {

    @Provides
    @DigineticaApiKey
    fun provideDigineticaApiKey(): String = BuildConfig.DIGINETICA_KEY
}
