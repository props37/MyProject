package ru.livetyping.zarina.application.extension

import android.app.Application
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import timber.log.Timber
import javax.inject.Inject

class TimberApplicationExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        if (BuildConfig.IS_LOGGING_ENABLED) Timber.plant(Timber.DebugTree())
    }
}
