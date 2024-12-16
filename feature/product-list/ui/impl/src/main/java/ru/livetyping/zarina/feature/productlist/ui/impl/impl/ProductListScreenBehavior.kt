package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavActions

@Composable
internal fun ProductListScreenBehavior(
    sideEffects: Flow<ProductListSideEffect>,
    navActions: ProductListNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProductListSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is ProductListSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: ProductListNavActions, action: ProductListScreenAction) {
    when (action) {
        ProductListScreenAction.BackClicked -> navActions.onBackClicked()
        is ProductListScreenAction.TagClicked -> {
            navActions.onTagClicked(action.tag, action.filters)
        }

        is ProductListScreenAction.ProductClicked -> navActions.onProductClicked(action.product)
        is ProductListScreenAction.SubscribeToProductClicked -> {
            navActions.onSubscribeToProductClicked(action.product, action.offer)
        }
    }
}
