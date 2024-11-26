package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import javax.inject.Inject

class PermissionManagerInitializer @Inject constructor(
    private val permissionManager: PermissionManager,
) : ActivityLifecycleObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val activity = source.asActivity()
        when (event) {
            Lifecycle.Event.ON_CREATE -> permissionManager.setActivity(activity)
            Lifecycle.Event.ON_DESTROY -> permissionManager.unsetActivity(activity)
            else -> Unit
        }
    }
}
