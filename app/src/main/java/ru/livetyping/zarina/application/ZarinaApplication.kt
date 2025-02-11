package ru.livetyping.zarina.application

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import ru.livetyping.zarina.application.extension.AppMetricaAppExtension
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var appMetricaAppExtension: AppMetricaAppExtension

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate")
        FirebaseApp.initializeApp(this)
        appMetricaAppExtension.install(this)
    }

    private companion object {
        private const val TAG = "ZarinaApplication"
    }
}
