package ru.zarina.zarina.application.extension

import android.app.Application
import android.content.Context
import android.os.Build
import coil.Coil
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import javax.inject.Inject

class CoilApplicationExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        val factory = ImageLoaderFactory {
            ImageLoader.Builder(application)
                .crossfade(true)
                .components {
                    addGifComponent()
                }
                .memoryCache(createMemoryCache(application))
                .diskCache(createDiskCache(application))
                .build()
        }

        Coil.setImageLoader(factory)
    }

    private fun ComponentRegistry.Builder.addGifComponent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }

    private fun createMemoryCache(context: Context): MemoryCache {
        return MemoryCache.Builder(context)
            .maxSizePercent(MEMORY_CACHE_MAX_SIZE_PERCENT)
            .build()
    }

    private fun createDiskCache(context: Context): DiskCache {
        return DiskCache.Builder()
            .directory(context.cacheDir.resolve(DISK_CACHE_DIRECTORY_NAME))
            .maxSizePercent(DISK_CACHE_MAX_SIZE_PERCENT)
            .build()
    }

    companion object {
        private const val MEMORY_CACHE_MAX_SIZE_PERCENT = 0.2

        private const val DISK_CACHE_DIRECTORY_NAME = "image_cache"
        private const val DISK_CACHE_MAX_SIZE_PERCENT = 0.02
    }
}
