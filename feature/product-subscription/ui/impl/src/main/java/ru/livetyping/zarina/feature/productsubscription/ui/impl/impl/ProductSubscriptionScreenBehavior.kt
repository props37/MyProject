package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.navigationutil.LifecycleSafeNavigator
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavActions

@Composable
internal fun ProductSubscriptionScreenBehavior(
    sideEffects: Flow<ProductSubscriptionSideEffect>,
    navActions: ProductSubscriptionNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val lifecycleSafeNavigator = LifecycleSafeNavigator()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProductSubscriptionSideEffect.Navigate -> {
                        lifecycleSafeNavigator.safeNavigate {
                            navigate(currentNavActions, sideEffect.action)
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

private fun navigate(
    navActions: ProductSubscriptionNavActions,
    action: ProductSubscriptionScreenAction
) {
    // TODO: [Top] Implement
    TODO()
}
