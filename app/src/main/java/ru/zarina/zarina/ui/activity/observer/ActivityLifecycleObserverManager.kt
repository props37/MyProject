package ru.zarina.zarina.ui.activity.observer

import javax.inject.Inject

class ActivityLifecycleObserverManager @Inject constructor(
    permissionManagerObserver: PermissionManagerActivityLifecycleObserver,
) {
    val observers = listOf(permissionManagerObserver)
}
