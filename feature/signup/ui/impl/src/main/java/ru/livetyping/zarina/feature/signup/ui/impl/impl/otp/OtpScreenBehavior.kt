package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior

@Composable
internal fun OtpScreenBehavior(
    sideEffects: Flow<OtpSideEffect>,
    navActions: OtpNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is OtpSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: OtpNavActions, action: OtpScreenAction) {
    // TODO: [Top] Implement
    TODO()
}
