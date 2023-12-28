package ru.zarina.zarina.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.zarina.zarina.application.extension.ApplicationExtensionManager
import ru.zarina.zarina.di.appModule
import ru.zarina.zarina.usecase.rework.authorization.FetchUnauthorizedUserAuthorizationTokensUseCase
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

@HiltAndroidApp
class ZarinaApplication : Application() {

    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var fetchUnauthorizedUserAuthorizationTokensUseCase: FetchUnauthorizedUserAuthorizationTokensUseCase

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
            fetchUnauthorizedUserAuthorizationTokensUseCase()
        }
    }
}
