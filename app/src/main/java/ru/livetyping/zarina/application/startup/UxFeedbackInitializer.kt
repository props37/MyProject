package ru.livetyping.zarina.application.startup

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import ru.livetyping.zarina.BuildConfig
import ru.uxfeedback.pub.sdk.UxFbSettings
import ru.uxfeedback.pub.sdk.UxFeedback

@SuppressLint("LogNotTimber")
class UxFeedbackInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val application = context as? Application
        if (application != null) {
            initializeUxFeedback(application)
            Log.v(TAG, "UxFeedback initialized")
        } else {
            Log.e(TAG, "Could not initialize UxFeedback because context was not an application")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    private fun initializeUxFeedback(application: Application) {
        try {
            val settings = UxFbSettings.getDefault().apply {
                debugEnabled = BuildConfig.IS_LOGGING_ENABLED
            }

            UxFeedback.setup(
                application = application,
                appId = BuildConfig.UX_FEEDBACK_APP_ID,
                settings = settings,
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize UxFeedback", e)
        }
    }

    private companion object {
        private const val TAG = "UxFeedbackInitializer"
    }
}