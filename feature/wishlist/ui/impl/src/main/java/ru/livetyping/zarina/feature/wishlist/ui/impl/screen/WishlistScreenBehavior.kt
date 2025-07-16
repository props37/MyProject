package ru.livetyping.zarina.feature.wishlist.ui.impl.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController2
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature

@Composable
internal fun WishlistScreenBehavior(
    sideEffects: Flow<WishlistSideEffect>,
    navActions: WishlistFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController2.current)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is WishlistSideEffect.Navigate -> navigate(currentNavActions, sideEffect.action)
                    is WishlistSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: WishlistFeature.NavActions, action: WishlistScreenAction) {
    when (action) {
        WishlistScreenAction.BackClicked -> navActions.onBackClicked()
        WishlistScreenAction.GoToCatalogClicked -> navActions.onGoToCatalogClicked()
        is WishlistScreenAction.ProductClicked -> navActions.onProductClicked(action.product)
        is WishlistScreenAction.CategoryShortcutClicked -> {
            navActions.onCategoryShortcutClicked(action.categoryId)
        }

        WishlistScreenAction.SearchClicked -> navActions.onSearchClicked()
    }
}
