package ru.livetyping.zarina.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExoPlayerModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoPlayerCache(
        @ApplicationContext
        context: Context,
    ): Cache {
        val cacheDir = File(context.cacheDir, EXO_PLAYER_CACHE_DIR)
        val evictor = LeastRecentlyUsedCacheEvictor(EXO_PLAYER_CACHE_MAX_BYTES)
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(
            /* cacheDir = */ cacheDir,
            /* evictor = */ evictor,
            /* databaseProvider = */ databaseProvider,
        )
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCacheDataSourceFactory(
        cache: Cache,
    ): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    companion object {
        private const val EXO_PLAYER_CACHE_DIR = "exo_player_cache"
        private const val EXO_PLAYER_CACHE_MAX_BYTES = 128L * 1024 * 1024
    }
}
