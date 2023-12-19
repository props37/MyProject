package ru.zarina.zarina

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.zarina.zarina.base.application.extension.base.ApplicationExtensionManager
import ru.zarina.zarina.di.appModule

class ZarinaApplication : Application() {
    private val applicationExtensionManager: ApplicationExtensionManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ZarinaApplication)
            modules(appModule)
        }

        installApplicationExtensions()
    }

    private fun installApplicationExtensions() {
        applicationExtensionManager.extensions.forEach { it.install(this) }
    }
}
