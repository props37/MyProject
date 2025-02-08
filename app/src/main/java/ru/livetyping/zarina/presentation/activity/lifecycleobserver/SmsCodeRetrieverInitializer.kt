package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import javax.inject.Inject

class SmsCodeRetrieverInitializer @Inject constructor(
    private val smsCodeRetriever: SmsCodeRetriever,
) : ActivityLifecycleObserver {
    override fun onStateChanged(activity: ComponentActivity, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                smsCodeRetriever.setActivityResultRegistry(activity.activityResultRegistry)
            }

            Lifecycle.Event.ON_DESTROY -> {
                smsCodeRetriever.unsetActivityResultRegistry(activity.activityResultRegistry)
            }

            else -> Unit
        }
    }
}
