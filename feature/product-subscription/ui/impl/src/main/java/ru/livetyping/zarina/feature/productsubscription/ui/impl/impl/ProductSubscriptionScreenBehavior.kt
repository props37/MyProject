package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavActions

@Composable
internal fun ProductSubscriptionScreenBehavior(
    sideEffects: Flow<ProductSubscriptionSideEffect>,
    navActions: ProductSubscriptionNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProductSubscriptionSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is ProductSubscriptionSideEffect.ShowZarinaToast -> {
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
    navActions: ProductSubscriptionNavActions,
    action: ProductSubscriptionScreenAction
) {
    when (action) {
        ProductSubscriptionScreenAction.BackClicked -> navActions.onBackClicked()
        ProductSubscriptionScreenAction.SubscriptionCompleted -> {
            navActions.onSubscriptionCompleted()
        }
    }
}
