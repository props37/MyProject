package ru.livetyping.zarina.ui.common.media.exoplayer

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource

val LocalExoPlayerCacheHolder = staticCompositionLocalOf<ExoPlayerCacheHolder?> { null }

@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayerCacheHolder(
    cache: Cache,
    cacheDataSourceFactory: CacheDataSource.Factory,
): ExoPlayerCacheHolder {
    return remember(cache, cacheDataSourceFactory) {
        ExoPlayerCacheHolderImpl(cache, cacheDataSourceFactory)
    }
}

@OptIn(UnstableApi::class)
interface ExoPlayerCacheHolder {
    val cache: Cache
    val cacheDataSourceFactory: CacheDataSource.Factory
}

@OptIn(UnstableApi::class)
class ExoPlayerCacheHolderImpl(
    override val cache: Cache,
    override val cacheDataSourceFactory: CacheDataSource.Factory,
) : ExoPlayerCacheHolder
