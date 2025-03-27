package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun PickupPointSelectorScreenBehavior(
    sideEffects: Flow<PickupPointSelectorSideEffect>,
    navActions: PickupPointSelectorNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PickupPointSelectorSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is PickupPointSelectorSideEffect.ShowZarinaToast -> {
                        currentZarinaToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(
    navActions: PickupPointSelectorNavActions,
    action: PickupPointSelectorScreenAction
) {
    // TODO: [Top] Implement
    TODO()
}
