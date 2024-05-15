package ru.livetyping.zarina.presentation.activity.lifecycleobserver

import javax.inject.Inject

class ActivityLifecycleObserverManager @Inject constructor(
    permissionManagerObserver: PermissionManagerActivityLifecycleObserver,
) {
    val observers = listOf(permissionManagerObserver)
}
