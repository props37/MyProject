package ru.zarina.zarina.usecase.exoplayer

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import ru.zarina.zarina.base.usecase.BasicUseCase
import ru.zarina.zarina.data.media.exoplayer.cache.ExoPlayerCacheProvider
import javax.inject.Inject

class GetExoPlayerCacheDataSourceFactoryUseCase @Inject constructor(
    private val exoPlayerCacheProvider: ExoPlayerCacheProvider,
) : BasicUseCase<Unit, CacheDataSource.Factory> {

    @OptIn(UnstableApi::class)
    override fun invoke(params: Unit): CacheDataSource.Factory {
        return exoPlayerCacheProvider.cacheDataSourceFactory
    }
}
