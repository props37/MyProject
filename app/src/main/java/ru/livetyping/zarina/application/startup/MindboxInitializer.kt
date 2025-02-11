package ru.livetyping.zarina.application.startup

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.startup.Initializer
import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import cloud.mindbox.mobile_sdk.logger.Level
import com.google.firebase.messaging.FirebaseMessaging
import ru.livetyping.zarina.BuildConfig

@SuppressLint("LogNotTimber")
class MindboxInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val configuration = getConfiguration(context)
        val pushServices = listOf(MindboxFirebase)
        Mindbox.initPushServices(context, pushServices)
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
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Mindbox.updatePushToken(context, token, MindboxFirebase)
        }
        Mindbox.subscribeDeviceUuid { uuid ->
            Log.v(MINDBOX_TAG, "Mindbox device UUID: $uuid")
        }
        Log.v(TAG, "Mindbox initialized")
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

    private companion object {
        private const val DOMAIN = "api.mindbox.ru"

        private const val TAG = "MindboxInitializer"
        private const val MINDBOX_TAG = "Mindbox"
    }
}
