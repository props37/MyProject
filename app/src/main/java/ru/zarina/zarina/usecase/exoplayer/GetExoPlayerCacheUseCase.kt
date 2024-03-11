package ru.zarina.zarina.usecase.exoplayer

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import ru.zarina.zarina.base.usecase.BasicUseCase
import ru.zarina.zarina.data.cache.ExoPlayerCacheProvider
import javax.inject.Inject

class GetExoPlayerCacheUseCase @Inject constructor(
    private val exoPlayerCacheHolder: ExoPlayerCacheProvider,
) : BasicUseCase<Unit, Cache> {

    @OptIn(UnstableApi::class)
    override fun invoke(params: Unit): Cache {
        return exoPlayerCacheHolder.cache
    }
}
