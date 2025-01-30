package ru.livetyping.zarina.application.extension

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import javax.inject.Inject

class AppMetricaApplicationExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        val config = AppMetricaConfig.newConfigBuilder(BuildConfig.APP_METRICA_KEY).apply {
            if (BuildConfig.IS_LOGGING_ENABLED) withLogs()
        }.build()
        AppMetrica.activate(application, config)
        AppMetrica.enableActivityAutoTracking(application)
    }
}