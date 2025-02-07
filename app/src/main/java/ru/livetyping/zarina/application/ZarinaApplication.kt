package ru.livetyping.zarina.application

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
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
        FirebaseApp.initializeApp(this)
        appMetricaExtension.install(this)
    }
}
