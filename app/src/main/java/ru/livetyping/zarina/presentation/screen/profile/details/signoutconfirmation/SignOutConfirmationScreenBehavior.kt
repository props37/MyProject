package ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.toastcontroller.LocalToastController
import ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation.SignOutConfirmationViewModel.SideEffect

@Composable
fun SignOutConfirmationScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (SignOutConfirmationScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedToastController by rememberUpdatedState(LocalToastController.current)

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

                    is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
