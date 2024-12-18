package ru.livetyping.zarina.feature.signin.ui.impl.impl.phoneconfirmation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior

@Composable
internal fun PhoneConfirmationScreenBehavior(
    sideEffects: Flow<PhoneConfirmationSideEffect>,
    navActions: PhoneConfirmationNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PhoneConfirmationSideEffect.Navigate -> {
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

private fun navigate(
    navActions: PhoneConfirmationNavActions,
    action: PhoneConfirmationScreenAction
) {
    // TODO: [Top] Implement
    TODO()
}
