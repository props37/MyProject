package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun OrderPlacingScreenBehavior(
    sideEffects: Flow<OrderPlacingSideEffect>,
    navActions: OrderPlacingNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentContext by rememberUpdatedState(LocalContext.current)
    val currentFocusManager by rememberUpdatedState(LocalFocusManager.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is OrderPlacingSideEffect.Navigate -> {
                        currentFocusManager.clearFocus()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    OrderPlacingSideEffect.HideKeyboard -> currentFocusManager.clearFocus()
                    is OrderPlacingSideEffect.ShowZarinaToast -> {
                        currentZarinaToastController.show(sideEffect.message)
                    }

                    is OrderPlacingSideEffect.OpenUrl -> {
                        currentContext.openUrlInCustomTabs(sideEffect.url.value)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: OrderPlacingNavActions, action: OrderPlacingScreenAction) {
    // TODO: [Top] Implement
    TODO()
}
