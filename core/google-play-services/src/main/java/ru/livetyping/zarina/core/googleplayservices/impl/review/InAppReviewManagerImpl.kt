package ru.livetyping.zarina.core.googleplayservices.impl.review

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.googleplayservices.review.InAppReviewManager
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

internal class InAppReviewManagerImpl(private val appMetrica: AppMetrica?) : InAppReviewManager {
    private val activityRef = AtomicReference<Activity?>(null)

    private val reviewManagerRef = AtomicReference<ReviewManager?>(null)

    override suspend fun launchReviewFlow() {
        val reviewManager = getReviewManager()
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
        reviewManagerRef.set(null)
        Timber.tag(TAG).v("Released")
    }

    private fun getReviewManager(): ReviewManager {
        val current = reviewManagerRef.get()
        return if (current != null) {
            current
        } else {
            val activity = requireActivity()
            val manager = ReviewManagerFactory.create(activity)
            val set = reviewManagerRef.compareAndSet(null, manager)
            if (set) {
                manager
            } else {
                checkNotNull(reviewManagerRef.get()) { "ReviewManager is null" }
            }
        }
    }

    private fun requireActivity(): Activity {
        val activity = activityRef.get()
        checkNotNull(activity) { "Activity can not be null. Did you forget to call setActivity?" }
        return activity
    }

    private companion object {
        private const val REVIEW_INFO_ERROR_MESSAGE = "Failed to request ReviewInfo"
        private const val REVIEW_FLOW_ERROR_MESSAGE = "Failed to launch review flow"

        private const val TAG = "InAppReviewManagerImpl"
    }
}
