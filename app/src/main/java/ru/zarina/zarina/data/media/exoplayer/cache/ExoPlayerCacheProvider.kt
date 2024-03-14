package ru.zarina.zarina.data.media.exoplayer.cache

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import javax.inject.Inject

@OptIn(UnstableApi::class)
class ExoPlayerCacheProvider @Inject constructor(
    val cache: Cache,
    val cacheDataSourceFactory: CacheDataSource.Factory,
)
