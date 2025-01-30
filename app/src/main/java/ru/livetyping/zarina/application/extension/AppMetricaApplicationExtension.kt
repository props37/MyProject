package ru.livetyping.zarina.application.extension

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import javax.inject.Inject

class AppMetricaApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getUserFlowUseCase: GetUserFlowUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        initializeAppMetrica(application)
        enableAppMetricaUserIdUpdate()
    }

    private fun initializeAppMetrica(application: Application) {
        val config = AppMetricaConfig.newConfigBuilder(BuildConfig.APP_METRICA_KEY).apply {
            if (BuildConfig.IS_LOGGING_ENABLED) withLogs()
        }.build()
        AppMetrica.activate(application, config)
        AppMetrica.enableActivityAutoTracking(application)
    }

    private fun enableAppMetricaUserIdUpdate() {
        getUserFlowUseCase()
            .map { result ->
                result.getOrNull()
            }
            .distinctUntilChanged()
            .onEach { user ->
                AppMetrica.setUserProfileID(user?.id?.value)
            }
            .launchIn(coroutineScope)
    }
}