package ru.livetyping.zarina.core.mediacompose

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource

@Suppress("ComposeCompositionLocalUsage")
public val LocalExoPlayerCacheDataSourceFactoryProvider: ProvidableCompositionLocal<ExoPlayerCacheDataSourceFactoryProvider?> =
    staticCompositionLocalOf { null }

@OptIn(UnstableApi::class)
@Composable
public fun rememberExoPlayerCacheDataSourceFactoryProvider(
    cacheDataSourceFactory: CacheDataSource.Factory,
): ExoPlayerCacheDataSourceFactoryProvider {
    return remember(cacheDataSourceFactory) {
        ExoPlayerCacheDataSourceFactoryProviderImpl(cacheDataSourceFactory)
    }
}

@OptIn(UnstableApi::class)
public interface ExoPlayerCacheDataSourceFactoryProvider {
    public fun provide(): CacheDataSource.Factory
}

@OptIn(UnstableApi::class)
internal class ExoPlayerCacheDataSourceFactoryProviderImpl(
    private val cacheDataSourceFactory: CacheDataSource.Factory,
) : ExoPlayerCacheDataSourceFactoryProvider {
    override fun provide(): CacheDataSource.Factory = cacheDataSourceFactory
}
