package ru.zarina.zarina.application.extension

import android.app.Application
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import timber.log.Timber
import javax.inject.Inject

class TimberExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        if (BuildConfig.IS_LOGGING_ENABLED) Timber.plant(Timber.DebugTree())
    }
}
