package ru.livetyping.zarina.core.googleplayservices.review

import android.app.Activity
import ru.livetyping.zarina.core.googleplayservices.impl.review.InAppReviewManagerImpl

public interface InAppReviewManager {
    public suspend fun launchReviewFlow()

    public fun setActivity(activity: Activity)

    public fun unsetActivity(activity: Activity)

    public fun release()

    public companion object {
        public fun createInstance(): InAppReviewManager {
            return InAppReviewManagerImpl()
        }
    }
}
