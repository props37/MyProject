package ru.zarina.zarina.base.application.extensions

import android.app.Application
import org.koin.core.annotation.Factory
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.base.application.extensions.base.ApplicationExtension
import timber.log.Timber
import javax.inject.Inject

@Factory
class TimberExtension @Inject constructor() : ApplicationExtension {

    override fun install(application: Application) {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}
