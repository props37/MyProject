package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import ru.livetyping.zarina.core.permission.PermissionManager
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import javax.inject.Inject

class PermissionManagerInitializer @Inject constructor(
    private val permissionManager: PermissionManager,
) : ActivityLifecycleObserver {
    override fun onStateChanged(activity: ComponentActivity, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> permissionManager.setActivity(activity)
            Lifecycle.Event.ON_DESTROY -> permissionManager.unsetActivity(activity)
            else -> Unit
        }
    }
}
