package ru.livetyping.zarina.application.extension

import android.app.Application
import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import cloud.mindbox.mobile_sdk.logger.Level
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import timber.log.Timber
import javax.inject.Inject

class MindboxApplicationExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        val configuration = getConfiguration(application)
        Mindbox.init(
            application = application,
            configuration = configuration,
            pushServices = listOf(MindboxFirebase),
        )
        if (BuildConfig.IS_LOGGING_ENABLED) {
            Mindbox.setLogLevel(Level.DEBUG)
        }
        Mindbox.subscribeDeviceUuid { uuid ->
            Timber.tag(TAG).v("Mindbox device UUID: $uuid")
        }
    }

    private fun getConfiguration(application: Application): MindboxConfiguration {
        return MindboxConfiguration.Builder(
            context = application,
            domain = DOMAIN,
            endpointId = BuildConfig.MINDBOX_ENDPOINT,
        )
            .shouldCreateCustomer(true)
            .subscribeCustomerIfCreated(true)
            .build()
    }

    companion object {
        const val DOMAIN = "api.mindbox.ru"

        private const val TAG = "Mindbox"
    }
}
