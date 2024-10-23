package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import ru.livetyping.zarina.core.uicommon.LifecycleEvent

@Composable
public fun LifecycleEventEffect(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onLifecycleEvent: (LifecycleEvent) -> Unit,
) {
    val updatedOnLifecycleEvent by rememberUpdatedState(onLifecycleEvent)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE, lifecycleOwner) {
        updatedOnLifecycleEvent(LifecycleEvent.ON_CREATE)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_START, lifecycleOwner) {
        updatedOnLifecycleEvent(LifecycleEvent.ON_START)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME, lifecycleOwner) {
        updatedOnLifecycleEvent(LifecycleEvent.ON_RESUME)
    }
}
