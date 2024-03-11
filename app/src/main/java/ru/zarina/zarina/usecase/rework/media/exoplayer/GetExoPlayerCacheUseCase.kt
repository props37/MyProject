package ru.zarina.zarina.usecase.rework.media.exoplayer

import androidx.media3.datasource.cache.Cache
import ru.zarina.zarina.data.rework.media.exoplayer.cache.ExoPlayerCacheProvider
import ru.zarina.zarina.base.usecase.BasicUseCase
import javax.inject.Inject

class GetExoPlayerCacheUseCase @Inject constructor(
    private val exoPlayerCacheHolder: ExoPlayerCacheProvider,
) : BasicUseCase<Unit, Cache> {

    override fun invoke(params: Unit): Cache {
        return exoPlayerCacheHolder.cache
    }
}
