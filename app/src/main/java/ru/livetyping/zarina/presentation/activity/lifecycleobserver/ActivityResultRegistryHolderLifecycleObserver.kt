package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import ru.livetyping.zarina.presentation.activity.ActivityResultRegistryHolder
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import javax.inject.Inject

class ActivityResultRegistryHolderLifecycleObserver @Inject constructor(
    private val activityResultRegistryHolder: ActivityResultRegistryHolder,
) : ActivityLifecycleObserver {
    override fun onStateChanged(activity: ComponentActivity, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                activityResultRegistryHolder.set(activity.activityResultRegistry)
            }

            Lifecycle.Event.ON_DESTROY -> {
                activityResultRegistryHolder.unset(activity.activityResultRegistry)
            }

            else -> Unit
        }
    }
}
