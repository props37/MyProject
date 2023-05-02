package ru.zarina.zarina.base.application.extensions

import android.app.Application
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.base.application.extensions.base.ApplicationExtension
import timber.log.Timber
import javax.inject.Inject

class TimberExtension @Inject constructor() : ApplicationExtension {

    override fun install(application: Application) {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}
