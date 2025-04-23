package ru.livetyping.zarina.core.googleplayservices.impl.review

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import ru.livetyping.zarina.core.googleplayservices.review.InAppReviewManager
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

internal class InAppReviewManagerImpl : InAppReviewManager {
    private val activityRef = AtomicReference<Activity?>(null)

    private val reviewManagerRef = AtomicReference<ReviewManager?>(null)

    override suspend fun launchReviewFlow() {
        val reviewManager = getReviewManager()
        val reviewInfo = try {
            reviewManager.requestReview()
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to request ReviewInfo")
            null
        }

        if (reviewInfo != null) {
            val activity = requireActivity()
            try {
                reviewManager.launchReview(activity, reviewInfo)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to launch review flow")
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
        private const val TAG = "InAppReviewManagerImpl"
    }
}
