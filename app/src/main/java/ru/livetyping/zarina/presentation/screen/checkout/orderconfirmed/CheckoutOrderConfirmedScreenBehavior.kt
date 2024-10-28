package ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.screen.checkout.orderconfirmed.CheckoutOrderConfirmedViewModel.SideEffect
import ru.livetyping.zarina.util.platform.dialPhoneNumber

@Composable
fun CheckoutOrderConfirmedScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutOrderConfirmedScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedContext by rememberUpdatedState(LocalContext.current)

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

                    is SideEffect.DialPhoneNumber -> {
                        updatedContext.dialPhoneNumber(sideEffect.phoneNumber.value)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
