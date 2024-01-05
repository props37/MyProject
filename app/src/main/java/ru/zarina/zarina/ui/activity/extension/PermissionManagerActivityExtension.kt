package ru.zarina.zarina.ui.activity.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.zarina.zarina.data.permissionmanager.PermissionManager
import ru.zarina.zarina.ui.activity.extension.base.ActivityExtension
import javax.inject.Inject

class PermissionManagerActivityExtension @Inject constructor(
    private val permissionManager: PermissionManager,
) : ActivityExtension {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val activity = source.asActivity()
        when (event) {
            Lifecycle.Event.ON_CREATE -> permissionManager.setActivity(activity)
            Lifecycle.Event.ON_DESTROY -> permissionManager.unsetActivity(activity)
            else -> Unit
        }
    }
}
