package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior

@Composable
internal fun CartScreenBehavior(
    onBackClicked: () -> Unit,
    sideEffects: Flow<CartSideEffect>,
    navActions: CartNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BackHandler(onBack = onBackClicked)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CartSideEffect.Navigate -> {
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

private fun navigate(navActions: CartNavActions, action: CartScreenAction) {
    when (action) {
        CartScreenAction.BackClicked -> navActions.onBackClicked()
    }
}
