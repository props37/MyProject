package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController

@Composable
internal fun ProductAvailabilityInStoresScreenBehavior(
    navigate: (ProductAvailabilityInStoresScreenAction) -> Unit,
    sideEffects: Flow<ProductAvailabilityInStoresViewModel.SideEffect>,
) {
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProductAvailabilityInStoresViewModel.SideEffect.Navigate -> {
                        navigate(sideEffect.action)
                    }

                    is ProductAvailabilityInStoresViewModel.SideEffect.ShowZarinaToast -> {
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
