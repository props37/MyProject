package ru.livetyping.zarina.core.feedback.impl

import android.app.Application
import android.util.Log
import ru.uxfeedback.pub.sdk.UxFbColor
import ru.uxfeedback.pub.sdk.UxFbSettings
import ru.uxfeedback.pub.sdk.UxFbTheme
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
            UxFeedback.sdk?.theme = UxFbTheme.fromStyle(R.style.UxFeedbackTheme)
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
            debugEnabled = isDebugEnabled
            if (isDebugEnabled) {
                startGlobalDelayTimer = 1
            }

            slideInUiBlocked = true
            slideInUiBlackoutColor = UxFbColor.fromHex(BLACKOUT_COLOR_HEX)
            slideInUiBlackoutOpacity = BLACKOUT_OPACITY
            slideInUiBlackoutBlur = BLACKOUT_BLUR

            popupUiBlackoutColor = UxFbColor.fromHex(BLACKOUT_COLOR_HEX)
            popupUiBlackoutOpacity = BLACKOUT_OPACITY
            popupUiBlackoutBlur = BLACKOUT_BLUR
        }
    }

    private const val BLACKOUT_COLOR_HEX = "#000000"
    private const val BLACKOUT_OPACITY = 50
    private const val BLACKOUT_BLUR = 40

    private const val USER_ID_KEY = "user_id"

    private const val TAG = "FeedbackInitializer"
}
