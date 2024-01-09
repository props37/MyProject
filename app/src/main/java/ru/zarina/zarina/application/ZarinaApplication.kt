package ru.zarina.zarina.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.zarina.zarina.application.extension.ApplicationExtensionManager
import ru.zarina.zarina.di.appModule
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var applicationExtensionManager: ApplicationExtensionManager

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
