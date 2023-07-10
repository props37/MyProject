package ru.zarina.zarina

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.zarina.zarina.base.application.extensions.base.ExtensionManager
import ru.zarina.zarina.di.appModule

class ZarinaApplication : Application() {

    private val extensionManager: ExtensionManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ZarinaApplication)
            modules(appModule)
        }

        installExtensions()
    }

    private fun installExtensions() {
        extensionManager.extensions.forEach { it.install(this) }
    }
}
