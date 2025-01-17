package ru.livetyping.zarina.presentation.screen.payment

import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.screen.payment.PaymentViewModel.SideEffect

@Composable
fun PaymentScreenBehavior(
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (PaymentScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)

    BackHandler(onBack = onBackClicked)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtimeMillis = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> {
                        safeNavigate(startedElapsedRealtimeMillis) {
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
