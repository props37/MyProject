package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect

@Composable
fun CheckoutOrderPlacingScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutOrderPlacingScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                    SideEffect.HideKeyboard -> updatedFocusManager.clearFocus()
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
