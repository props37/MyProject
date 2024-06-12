package ru.livetyping.zarina.presentation.screen.profile.details.accountdeletionconfirmation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.toastcontroller.LocalToastController
import ru.livetyping.zarina.presentation.screen.profile.details.accountdeletionconfirmation.AccountDeletionConfirmationViewModel.SideEffect

@Composable
fun AccountDeletionConfirmationScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (AccountDeletionConfirmationScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedToastController by rememberUpdatedState(LocalToastController.current)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                    is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
