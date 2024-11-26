package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.livetyping.zarina.core.googleplayservices.sms.SmsCodeRetriever
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import javax.inject.Inject

class SmsCodeRetrieverInitializer @Inject constructor(
    private val smsCodeRetriever: SmsCodeRetriever,
) : ActivityLifecycleObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val activity = source.asActivity()
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
