package ru.livetyping.zarina.application.startup

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import ru.livetyping.zarina.BuildConfig
import timber.log.Timber

@SuppressLint("LogNotTimber")
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.IS_LOGGING_ENABLED) Timber.plant(Timber.DebugTree())
        Log.v(TAG, "Timber initialized")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    private companion object {
        private const val TAG = "TimberInitializer"
    }
}
