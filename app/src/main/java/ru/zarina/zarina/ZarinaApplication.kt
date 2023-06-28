package ru.zarina.zarina

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module
import ru.zarina.zarina.base.application.extensions.base.ExtensionManager
import ru.zarina.zarina.di.CoroutineModule
import ru.zarina.zarina.di.DataStoreModule
import ru.zarina.zarina.di.DatabaseModule
import ru.zarina.zarina.di.NetworkModule
import ru.zarina.zarina.di.PlayServicesModule
import ru.zarina.zarina.di.PlayerModule

class ZarinaApplication : Application() {

    private val extensionManager: ExtensionManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ZarinaApplication)
            modules(
                defaultModule,
                CoroutineModule().module,
                DatabaseModule().module,
                DataStoreModule().module,
                NetworkModule().module,
                PlayerModule().module,
                PlayServicesModule().module
            )
        }

        installExtensions()
    }

    private fun installExtensions() {
        extensionManager.extensions.forEach { it.install(this) }
    }
}
