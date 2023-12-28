package ru.zarina.zarina.application.extension

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.koin.core.annotation.Factory
import ru.zarina.zarina.application.extension.base.ApplicationExtension

@Factory
class CoilExtension : ApplicationExtension {
    override fun install(application: Application) {
        val factory = ImageLoaderFactory {
            ImageLoader.Builder(application)
                .crossfade(true)
                .memoryCache {
                    MemoryCache.Builder(application)
                        .maxSizePercent(MEMORY_CACHE_MAX_SIZE_PERCENT)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(application.cacheDir.resolve(DISK_CACHE_DIRECTORY_NAME))
                        .maxSizePercent(DISK_CACHE_MAX_SIZE_PERCENT)
                        .build()
                }
                .build()
        }

        Coil.setImageLoader(factory)
    }

    companion object {
        private const val MEMORY_CACHE_MAX_SIZE_PERCENT = 0.2

        private const val DISK_CACHE_DIRECTORY_NAME = "image_cache"
        private const val DISK_CACHE_MAX_SIZE_PERCENT = 0.02
    }
}
