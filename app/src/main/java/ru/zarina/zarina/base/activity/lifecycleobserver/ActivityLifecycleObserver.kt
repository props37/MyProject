package ru.zarina.zarina.base.activity.lifecycleobserver

import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface ActivityLifecycleObserver : LifecycleEventObserver {
    fun LifecycleOwner.asActivity(): ComponentActivity {
        return checkNotNull(this as? ComponentActivity) {
            "ActivityLifecycleObserver should be attached to Activity lifecycle"
        }
    }
}
