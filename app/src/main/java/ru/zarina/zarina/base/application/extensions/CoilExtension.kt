package ru.zarina.zarina.base.application.extensions

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import org.koin.core.annotation.Factory
import ru.zarina.zarina.base.application.extensions.base.ApplicationExtension

@Factory
class CoilExtension : ApplicationExtension {

    override fun install(application: Application) {
        val factory = ImageLoaderFactory {
            ImageLoader.Builder(application)
                .memoryCache {
                    MemoryCache.Builder(application)
                        .maxSizePercent(MEMORY_CACHE_FRACTION)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(application.cacheDir.resolve(IMAGE_CACHE_DIR_NAME))
                        .maxSizePercent(DISK_CACHE_FRACTION)
                        .build()
                }
                .build()
        }

        Coil.setImageLoader(factory)
    }

    companion object {
        private const val IMAGE_CACHE_DIR_NAME = "image_cache"
        private const val MEMORY_CACHE_FRACTION = 0.2
        private const val DISK_CACHE_FRACTION = 0.02
    }
}