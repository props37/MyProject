package ru.zarina.zarina.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import java.io.File

@Module
class PlayerModule {

    @androidx.annotation.OptIn(UnstableApi::class)
    @Singleton
    fun providesCache(
        context: Context,
    ): Cache {
        val cacheFile = File(context.cacheDir, EXOPLAYER_CACHE_DIR)
        val evictor = LeastRecentlyUsedCacheEvictor(EXOPLAYER_CACHE_SIZE)
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(
            /* cacheDir = */ cacheFile,
            /* evictor = */ evictor,
            /* databaseProvider = */ databaseProvider
        )
    }

    companion object {
        private const val EXOPLAYER_CACHE_DIR = "exoplayer-cache"
        private const val EXOPLAYER_CACHE_SIZE = 128L * 1024 * 1024
    }

}
