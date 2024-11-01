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
    val currentOnLifecycleEvent by rememberUpdatedState(onLifecycleEvent)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE, lifecycleOwner) {
        currentOnLifecycleEvent(LifecycleEvent.ON_CREATE)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_START, lifecycleOwner) {
        currentOnLifecycleEvent(LifecycleEvent.ON_START)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME, lifecycleOwner) {
        currentOnLifecycleEvent(LifecycleEvent.ON_RESUME)
    }
}
