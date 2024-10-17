package ru.livetyping.zarina.presentation.screen.permissionrequirement

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.systemsettings.openSettings
import ru.livetyping.zarina.presentation.screen.permissionrequirement.PermissionRequirementViewModel.SideEffect

@Composable
fun PermissionRequirementScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (PermissionRequirementScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> {
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigate(sideEffect.action)
                        }
                    }

                    is SideEffect.OpenSystemSettings -> {
                        updatedContext.openSettings(sideEffect.settings)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
