package ru.livetyping.zarina.presentation.screen.profile.details.signoutconfirmation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                        is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
