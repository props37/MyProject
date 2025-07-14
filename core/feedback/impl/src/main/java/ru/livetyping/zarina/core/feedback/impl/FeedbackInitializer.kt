package ru.livetyping.zarina.core.feedback.impl

import android.app.Application
import android.util.Log
import ru.uxfeedback.pub.sdk.UxFbSettings
import ru.uxfeedback.pub.sdk.UxFeedback

public object FeedbackInitializer {
    public fun initialize(
        application: Application,
        appId: String,
        isDebugEnabled: Boolean,
    ) {
        try {
            UxFeedback.setup(
                application = application,
                appId = appId,
                settings = getSettings(isDebugEnabled),
            )
            Log.v(TAG, "UxFeedback initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize UxFeedback", e)
        }
    }

    public fun updateUserId(id: String?) {
        if (id != null) {
            UxFeedback.sdk?.properties?.put(USER_ID_KEY, id)
        } else {
            UxFeedback.sdk?.properties?.remove(USER_ID_KEY)
        }
    }

    private fun getSettings(isDebugEnabled: Boolean): UxFbSettings {
        return UxFbSettings.getDefault().apply {
            slideInUiBlocked = true

            debugEnabled = isDebugEnabled
            if (isDebugEnabled) {
                startGlobalDelayTimer = 1
            }
        }
    }

    private const val USER_ID_KEY = "user_id"

    private const val TAG = "FeedbackInitializer"
}
