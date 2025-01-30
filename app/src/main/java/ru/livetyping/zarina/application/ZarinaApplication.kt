package ru.livetyping.zarina.application

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import ru.livetyping.zarina.application.extension.ApplicationExtensionManager
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var applicationExtensionManager: ApplicationExtensionManager

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        installApplicationExtensions()
    }

    private fun installApplicationExtensions() {
        applicationExtensionManager.extensions.forEach { it.install(this) }
    }
}
