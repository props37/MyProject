package ru.livetyping.zarina.core.media.impl

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

private const val CACHE_DIR = "exo_player_cache"
private const val CACHE_MAX_BYTES = 128L * 1024 * 1024

@OptIn(UnstableApi::class)
internal fun getCache(context: Context): Cache {
    val cacheDir = File(context.cacheDir, CACHE_DIR)
    val evictor = LeastRecentlyUsedCacheEvictor(CACHE_MAX_BYTES)
    val databaseProvider = StandaloneDatabaseProvider(context)
    return SimpleCache(
        /* cacheDir = */ cacheDir,
        /* evictor = */ evictor,
        /* databaseProvider = */ databaseProvider,
    )
}
