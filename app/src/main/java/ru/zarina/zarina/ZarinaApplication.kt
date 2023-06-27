package ru.zarina.zarina

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import ru.zarina.zarina.base.application.extensions.base.ExtensionManager

@HiltAndroidApp
class ZarinaApplication : Application() {

    private val extensionManager: ExtensionManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ZarinaApplication)
            defaultModule()
        }

        installExtensions()
    }

    private fun installExtensions() {
        extensionManager.extensions.forEach { it.install(this) }
    }
}
