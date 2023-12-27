package ru.zarina.zarina.base.application.extension

import android.app.Application
import org.koin.core.annotation.Factory
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.base.application.extension.base.ApplicationExtension
import timber.log.Timber

@Factory
class TimberExtension : ApplicationExtension {
    override fun install(application: Application) {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
