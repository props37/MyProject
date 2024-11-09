package ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreViewModel.SideEffect

@Composable
fun CheckoutSelectedPickupStoreScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutSelectedPickupStoreScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

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
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
