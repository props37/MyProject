package ru.zarina.zarina

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.zarina.zarina.base.application.extensions.base.ExtensionManager
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var extensionManager: ExtensionManager

    override fun onCreate() {
        super.onCreate()
        installExtensions()
    }

    private fun installExtensions() {
        extensionManager.extensions.forEach { it.install(this) }
    }
}
