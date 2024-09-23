package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.SideEffect

@Composable
fun CheckoutOrderPlacingScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutOrderPlacingScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

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
