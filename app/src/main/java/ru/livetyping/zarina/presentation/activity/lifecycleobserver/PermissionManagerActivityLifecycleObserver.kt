package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver
import ru.livetyping.zarina.presentation.common.permissionmanager.PermissionManager
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
