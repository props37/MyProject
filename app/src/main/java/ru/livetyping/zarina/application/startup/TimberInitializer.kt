package ru.livetyping.zarina.application.startup

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import ru.livetyping.zarina.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Log.d("TimberInitializer", "Initialize Timber")
        if (BuildConfig.IS_LOGGING_ENABLED) Timber.plant(Timber.DebugTree())
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
