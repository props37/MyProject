package ru.zarina.zarina.ui.common.media.exoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheDataSource.Factory

val LocalExoPlayerCacheHolder = staticCompositionLocalOf<ExoPlayerCacheHolder?> { null }

@Composable
fun rememberExoPlayerCacheHolder(
    cache: Cache,
    cacheDataSourceFactory: Factory,
): ExoPlayerCacheHolder {
    return remember(cache, cacheDataSourceFactory) {
        ExoPlayerCacheHolderImpl(cache, cacheDataSourceFactory)
    }
}

interface ExoPlayerCacheHolder {
    val cache: Cache
    val cacheDataSourceFactory: CacheDataSource.Factory
}

class ExoPlayerCacheHolderImpl(
    override val cache: Cache,
    override val cacheDataSourceFactory: CacheDataSource.Factory,
) : ExoPlayerCacheHolder
