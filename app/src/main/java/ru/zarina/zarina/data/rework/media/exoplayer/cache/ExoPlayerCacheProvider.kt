package ru.zarina.zarina.data.rework.media.exoplayer.cache

import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import javax.inject.Inject

class ExoPlayerCacheProvider @Inject constructor(
    val cache: Cache,
    val cacheDataSourceFactory: CacheDataSource.Factory,
)
