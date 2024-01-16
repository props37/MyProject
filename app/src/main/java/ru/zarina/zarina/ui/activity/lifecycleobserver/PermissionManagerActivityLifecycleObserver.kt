package ru.zarina.zarina.ui.activity.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.zarina.zarina.data.rework.permissionmanager.PermissionManager
import ru.zarina.zarina.ui.activity.lifecycleobserver.base.ActivityLifecycleObserver
import javax.inject.Inject

class PermissionManagerActivityLifecycleObserver @Inject constructor(
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
