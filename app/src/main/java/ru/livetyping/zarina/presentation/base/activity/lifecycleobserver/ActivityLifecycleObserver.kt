package ru.livetyping.zarina.presentation.base.activity.lifecycleobserver

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface ActivityLifecycleObserver : LifecycleEventObserver {
    fun onStateChanged(activity: ComponentActivity, event: Lifecycle.Event)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val activity = source.asActivity()
        onStateChanged(activity, event)
    }

    private fun LifecycleOwner.asActivity(): ComponentActivity {
        return checkNotNull(this as? ComponentActivity) {
            "ActivityLifecycleObserver should be attached to Activity lifecycle"
        }
    }
}
