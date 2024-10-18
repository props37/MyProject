package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl

@Module
@InstallIn(SingletonComponent::class)
internal class ZarinaBaseUrlModule {

    @Provides
    @ZarinaBaseUrl
    fun provideZarinaBaseUrl(): String = BuildConfig.BACKEND_URL
}
