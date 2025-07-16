package ru.livetyping.zarina.core.googleplayservices.review

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import ru.livetyping.zarina.core.analytics.AppMetrica
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class InAppReviewManagerImpl(
    context: Context,
    private val appMetrica: AppMetrica?
) : InAppReviewManager {
    private val activityRef = AtomicReference<Activity?>(null)

    private val reviewManager = ReviewManagerFactory.create(context)

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override suspend fun launchReviewFlow(minTimeSinceLastLaunch: Duration) {
        if (!canLaunchReviewFlow(minTimeSinceLastLaunch)) {
            Timber.tag(TAG).v("Could not launch review flow since it is too soon")
            return
        }

        val reviewInfo = try {
            reviewManager.requestReview()
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, REVIEW_INFO_ERROR_MESSAGE)
            appMetrica?.reportError(InAppReviewManager.ERROR_TAG, REVIEW_INFO_ERROR_MESSAGE, e)
            null
        }

        if (reviewInfo != null) {
            val activity = requireActivity()
            try {
                reviewManager.launchReview(activity, reviewInfo)
                setLastLaunchTimestampEpochMillis(System.currentTimeMillis())
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, REVIEW_FLOW_ERROR_MESSAGE)
                appMetrica?.reportError(InAppReviewManager.ERROR_TAG, REVIEW_FLOW_ERROR_MESSAGE, e)
            }
        }
    }

    override fun setActivity(activity: Activity) {
        activityRef.set(activity)
        Timber.tag(TAG).v("Activity set")
    }

    override fun unsetActivity(activity: Activity) {
        val unset = activityRef.compareAndSet(activity, null)
        Timber.tag(TAG).v("Activity unset: $unset")
    }

    override fun release() {
        activityRef.set(null)
        Timber.tag(TAG).v("Released")
    }

    private fun canLaunchReviewFlow(minTimeSinceLastLaunch: Duration): Boolean {
        val lastLaunchTimestampMillis = getLastLaunchTimestampEpochMillis() ?: return true
        val timeSinceLastLaunch = (System.currentTimeMillis() - lastLaunchTimestampMillis)
            .coerceAtLeast(0)
            .milliseconds
        return timeSinceLastLaunch >= minTimeSinceLastLaunch
    }

    private fun getLastLaunchTimestampEpochMillis(): Long? {
        val defValue = -1L
        val millis = sharedPreferences.getLong(LAST_LAUNCH_TIMESTAMP_EPOCH_MILLIS_KEY, defValue)
        return if (millis != defValue) millis else null
    }

    private fun setLastLaunchTimestampEpochMillis(millis: Long) {
        sharedPreferences.edit {
            putLong(LAST_LAUNCH_TIMESTAMP_EPOCH_MILLIS_KEY, millis)
        }
    }

    private fun requireActivity(): Activity {
        val activity = activityRef.get()
        checkNotNull(activity) { "Activity can not be null. Did you forget to call setActivity?" }
        return activity
    }

    private companion object {
        private const val SHARED_PREFERENCES_NAME = "in_app_review_manager_preferences"
        private const val LAST_LAUNCH_TIMESTAMP_EPOCH_MILLIS_KEY =
            "last_launch_timestamp_epoch_millis"

        private const val REVIEW_INFO_ERROR_MESSAGE = "Failed to request ReviewInfo"
        private const val REVIEW_FLOW_ERROR_MESSAGE = "Failed to launch review flow"

        private const val TAG = "InAppReviewManagerImpl"
    }
}
