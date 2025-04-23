package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import ru.livetyping.zarina.core.googleplayservices.review.InAppReviewManager
import javax.inject.Inject

class InAppReviewManagerInitializer @Inject constructor(
    private val inAppReviewManager: InAppReviewManager,
) : ActivityLifecycleObserver {
    override fun onStateChanged(activity: ComponentActivity, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> inAppReviewManager.setActivity(activity)
            Lifecycle.Event.ON_DESTROY -> inAppReviewManager.unsetActivity(activity)
            else -> Unit
        }
    }
}
