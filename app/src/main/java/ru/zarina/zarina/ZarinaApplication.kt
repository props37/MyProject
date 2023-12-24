package ru.zarina.zarina

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.zarina.zarina.base.application.extension.base.ApplicationExtensionManager
import ru.zarina.zarina.di.appModule
import ru.zarina.zarina.usecase.rework.authorization.FetchUnauthorizedUserAuthorizationTokensUseCase
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var fetchUnauthorizedUserAuthorizationTokens: FetchUnauthorizedUserAuthorizationTokensUseCase

    private val applicationExtensionManager: ApplicationExtensionManager by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ZarinaApplication)
            modules(appModule)
        }

        installApplicationExtensions()

        fetchUnauthorizedUserAuthorizationTokens()
    }

    private fun installApplicationExtensions() {
        applicationExtensionManager.extensions.forEach { it.install(this) }
    }

    private fun fetchUnauthorizedUserAuthorizationTokens() {
        coroutineScope.launch {
            fetchUnauthorizedUserAuthorizationTokens()
        }
    }
}
