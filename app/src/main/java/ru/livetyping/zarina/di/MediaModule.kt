package ru.livetyping.zarina.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.media.CacheCreator
import ru.livetyping.zarina.core.media.CacheDataSourceFactoryCreator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MediaModule {
    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCacheDataSourceFactory(
        cache: Cache,
    ): CacheDataSource.Factory {
        return CacheDataSourceFactoryCreator.create(cache)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCache(
        @ApplicationContext
        context: Context,
    ): Cache {
        return CacheCreator.create(context)
    }
}
