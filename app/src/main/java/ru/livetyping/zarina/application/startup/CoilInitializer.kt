package ru.livetyping.zarina.application.startup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.startup.Initializer
import coil.Coil
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache

@SuppressLint("LogNotTimber")
class CoilInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val factory = ImageLoaderFactory {
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    addGifComponent()
                }
                .memoryCache(createMemoryCache(context))
                .diskCache(createDiskCache(context))
                .build()
        }

        Coil.setImageLoader(factory)
        Log.v(TAG, "Coil initialized")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
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
            .directory(context.cacheDir.resolve(DISK_CACHE_DIR))
            .maxSizePercent(DISK_CACHE_MAX_SIZE_PERCENT)
            .build()
    }

    companion object {
        private const val MEMORY_CACHE_MAX_SIZE_PERCENT = 0.2

        private const val DISK_CACHE_DIR = "coil_cache"
        private const val DISK_CACHE_MAX_SIZE_PERCENT = 0.02

        private const val TAG = "CoilInitializer"
    }
}
