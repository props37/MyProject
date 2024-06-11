package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import ru.livetyping.zarina.presentation.activity.ActivityResultRegistryHolder
import javax.inject.Inject

class ActivityResultRegistryHolderLifecycleObserver @Inject constructor(
    private val activityResultRegistryHolder: ActivityResultRegistryHolder,
) : ActivityLifecycleObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val activity = source.asActivity()
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
