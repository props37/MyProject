package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordViewModel.SideEffect

@Composable
fun ChangePasswordScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ChangePasswordScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> {
                        updatedKeyboardController?.hide()
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigate(sideEffect.action)
                        }
                    }

                    is SideEffect.ShowZarinaToast -> {
                        updatedZarinaToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
