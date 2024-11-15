package ru.livetyping.zarina.application.extension

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import cloud.mindbox.mobile_sdk.logger.Level
import com.google.firebase.messaging.FirebaseMessaging
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import timber.log.Timber
import javax.inject.Inject

class MindboxApplicationExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        val configuration = getConfiguration(application)
        val pushServices = listOf(MindboxFirebase)
        Mindbox.initPushServices(
            context = application,
            pushServices = pushServices,
        )
        Mindbox.init(
            application = application,
            configuration = configuration,
            pushServices = pushServices,
        )
        if (BuildConfig.IS_LOGGING_ENABLED) {
            Mindbox.setLogLevel(Level.DEBUG)
        }
        val areNotificationsEnabled =
            NotificationManagerCompat.from(application).areNotificationsEnabled()
        if (areNotificationsEnabled) {
            Mindbox.updateNotificationPermissionStatus(application)
        }
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Mindbox.updatePushToken(application, token, MindboxFirebase)
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
