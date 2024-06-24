package ru.livetyping.zarina.presentation.screen.profile.details.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.screen.profile.details.changepassword.ChangePasswordViewModel.SideEffect

@Composable
fun ChangePasswordScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ChangePasswordScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
