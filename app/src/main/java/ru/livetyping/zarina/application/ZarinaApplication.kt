package ru.livetyping.zarina.application

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import ru.livetyping.zarina.application.extension.AppMetricaApplicationExtension
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var appMetricaExtension: AppMetricaApplicationExtension

    override fun onCreate() {
        super.onCreate()
        Log.v("ZarinaApplication", "onCreate")
        appMetricaExtension.install(this)
    }

    companion object {
        const val RELEASE_PACKAGE_NAME = "ru.livetyping.zarina"
    }
}
