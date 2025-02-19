package ru.livetyping.zarina.application.startup

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.startup.Initializer
import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import cloud.mindbox.mobile_sdk.logger.Level
import ru.livetyping.zarina.BuildConfig

class MindboxInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Log.d(TAG, "Initialize Mindbox")
        val configuration = getConfiguration(context)
        val pushServices = listOf(MindboxFirebase)
        Mindbox.initPushServices(
            context = context,
            pushServices = pushServices,
        )
        Mindbox.init(
            application = context as Application,
            configuration = configuration,
            pushServices = pushServices,
        )
        if (BuildConfig.IS_LOGGING_ENABLED) {
            Mindbox.setLogLevel(Level.DEBUG)
        }
        val areNotificationsEnabled =
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (areNotificationsEnabled) {
            Mindbox.updateNotificationPermissionStatus(context)
        }
        Mindbox.subscribeDeviceUuid { uuid ->
            Log.v(MINDBOX_TAG, "Mindbox device UUID: $uuid")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    private fun getConfiguration(context: Context): MindboxConfiguration {
        return MindboxConfiguration.Builder(
            context = context,
            domain = DOMAIN,
            endpointId = BuildConfig.MINDBOX_ENDPOINT,
        )
            .shouldCreateCustomer(true)
            .subscribeCustomerIfCreated(true)
            .build()
    }

    companion object {
        const val DOMAIN = "api.mindbox.ru"

        private const val TAG = "MindboxInitializer"
        private const val MINDBOX_TAG = "Mindbox"
    }
}
