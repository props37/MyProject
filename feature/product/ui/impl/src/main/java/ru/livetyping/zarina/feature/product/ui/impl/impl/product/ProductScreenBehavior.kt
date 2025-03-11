package ru.livetyping.zarina.feature.product.ui.impl.impl.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.platform.shareText
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun ProductScreenBehavior(
    sideEffects: Flow<ProductSideEffect>,
    navActions: ProductNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentContext by rememberUpdatedState(LocalContext.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProductSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is ProductSideEffect.Share -> currentContext.shareText(sideEffect.text)
                    is ProductSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: ProductNavActions, action: ProductScreenAction) {
    when (action) {
        ProductScreenAction.BackClicked -> navActions.onBackClicked()
        is ProductScreenAction.SubscribeToProductClicked -> {
            navActions.onSubscribeToProductClicked(action.product, action.offer)
        }

        is ProductScreenAction.ProductClicked -> navActions.onProductClicked(action.product)
    }
}
