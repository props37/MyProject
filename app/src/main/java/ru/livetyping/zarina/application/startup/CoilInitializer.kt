package ru.livetyping.zarina.application.startup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.startup.Initializer
import coil3.ComponentRegistry
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.memory.MemoryCache
import coil3.request.crossfade

@SuppressLint("LogNotTimber")
class CoilInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        SingletonImageLoader.setSafe { platformContext ->
            ImageLoader.Builder(platformContext)
                .crossfade(true)
                .components {
                    addGifComponent()
                }
                .memoryCache(createMemoryCache(platformContext))
                .diskCache(createDiskCache(platformContext))
                .build()
        }
        Log.v(TAG, "Coil initialized")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    private fun ComponentRegistry.Builder.addGifComponent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(AnimatedImageDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }

    private fun createMemoryCache(context: Context): MemoryCache {
        return MemoryCache.Builder()
            .maxSizePercent(context, MEMORY_CACHE_MAX_SIZE_PERCENT)
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
