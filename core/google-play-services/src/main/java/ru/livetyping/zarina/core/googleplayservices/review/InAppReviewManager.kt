package ru.livetyping.zarina.core.googleplayservices.review

import android.app.Activity
import android.content.Context
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.googleplayservices.impl.review.InAppReviewManagerImpl
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

public interface InAppReviewManager {
    public suspend fun launchReviewFlow(
        minTimeSinceLastLaunch: Duration = MIN_TIME_BETWEEN_REVIEW_FLOW_LAUNCHES_IN_DAYS.days,
    )

    public fun setActivity(activity: Activity)

    public fun unsetActivity(activity: Activity)

    public fun release()

    public companion object {
        public const val MIN_TIME_BETWEEN_REVIEW_FLOW_LAUNCHES_IN_DAYS: Int = 30

        public fun createInstance(
            context: Context,
            appMetrica: AppMetrica?,
        ): InAppReviewManager {
            return InAppReviewManagerImpl(context, appMetrica)
        }

        internal const val ERROR_TAG = "InAppReview"
    }
}
