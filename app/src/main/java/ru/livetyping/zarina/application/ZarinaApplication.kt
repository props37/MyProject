package ru.livetyping.zarina.application

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var applicationExtensions: Set<@JvmSuppressWildcards ApplicationExtension>

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        installApplicationExtensions()
    }

    private fun installApplicationExtensions() {
        applicationExtensions.forEach { extension ->
            extension.install(this)
        }
    }
}
