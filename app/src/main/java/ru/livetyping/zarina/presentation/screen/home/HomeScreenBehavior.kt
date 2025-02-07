package ru.livetyping.zarina.presentation.screen.home

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.screen.home.HomeViewModel.SideEffect

@Composable
fun HomeScreenBehavior(
    onScreenCreated: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateForward: (HomeScreenAction) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        onScreenCreated()
    }

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.NavigateForward -> {
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigateForward(sideEffect.action)
                        }
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

