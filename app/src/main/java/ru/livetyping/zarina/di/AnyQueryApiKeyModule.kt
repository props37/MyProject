package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.AnyQueryApiKey

@Module
@InstallIn(SingletonComponent::class)
class AnyQueryApiKeyModule {

    @Provides
    @AnyQueryApiKey
    fun provideAnyQueryApiKey(): String = BuildConfig.ANY_QUERY_KEY
}
