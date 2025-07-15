package ru.livetyping.zarina.application.startup

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.feedback.impl.FeedbackInitializer as FeedbackInitializerImpl

@SuppressLint("LogNotTimber")
class FeedbackInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val application = context as? Application
        if (application != null) {
            FeedbackInitializerImpl.initialize(
                application = application,
                appId = BuildConfig.UX_FEEDBACK_APP_ID,
                isDebugEnabled = BuildConfig.IS_LOGGING_ENABLED,
            )
            Log.v(TAG, "Feedback initialized")
        } else {
            Log.e(TAG, "Could not initialize Feedback because context was not an application")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    private companion object {
        private const val TAG = "FeedbackInitializer"
    }
}