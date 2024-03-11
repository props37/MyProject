package ru.zarina.zarina.usecase.rework.media.exoplayer

import androidx.media3.datasource.cache.CacheDataSource
import ru.zarina.zarina.data.rework.media.exoplayer.cache.ExoPlayerCacheProvider
import ru.zarina.zarina.base.usecase.BasicUseCase
import javax.inject.Inject

class GetExoPlayerCacheDataSourceFactoryUseCase @Inject constructor(
    private val exoPlayerCacheProvider: ExoPlayerCacheProvider,
) : BasicUseCase<Unit, CacheDataSource.Factory> {

    override fun invoke(params: Unit): CacheDataSource.Factory {
        return exoPlayerCacheProvider.cacheDataSourceFactory
    }
}
