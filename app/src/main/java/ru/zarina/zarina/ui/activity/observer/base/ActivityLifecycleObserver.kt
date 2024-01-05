package ru.zarina.zarina.ui.activity.observer.base

import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface ActivityLifecycleObserver : LifecycleEventObserver {
    fun LifecycleOwner.asActivity(): ComponentActivity {
        return checkNotNull(this as? ComponentActivity) {
            "ActivityExtension should be attached to Activity lifecycle"
        }
    }
}
