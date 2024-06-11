package ru.livetyping.zarina.presentation.activity

import androidx.activity.result.ActivityResultRegistry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityResultRegistryHolder @Inject constructor() {

    @Volatile
    var activityResultRegistry: ActivityResultRegistry? = null
        private set

    @Synchronized
    fun set(registry: ActivityResultRegistry) {
        activityResultRegistry = registry
    }

    @Synchronized
    fun unset(registry: ActivityResultRegistry) {
        if (registry == activityResultRegistry) {
            activityResultRegistry = null
        }
    }
}
