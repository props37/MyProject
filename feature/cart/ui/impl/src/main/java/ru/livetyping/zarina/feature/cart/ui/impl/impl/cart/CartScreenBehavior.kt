package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun CartScreenBehavior(
    onBackClicked: () -> Unit,
    onScreenCreated: () -> Unit,
    sideEffects: Flow<CartSideEffect>,
    navActions: CartNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentFocusManager by rememberUpdatedState(LocalFocusManager.current)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentOnScreenCreated by rememberUpdatedState(onScreenCreated)

    BackHandler(onBack = onBackClicked)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        currentOnScreenCreated()
    }

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CartSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    CartSideEffect.HideKeyboard -> currentFocusManager.clearFocus()

                    is CartSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: CartNavActions, action: CartScreenAction) {
    when (action) {
        CartScreenAction.BackClicked -> navActions.onBackClicked()
        is CartScreenAction.CheckoutClicked -> Unit // TODO: [Top] Implement
        is CartScreenAction.ChangeCityClicked -> navActions.onChangeCityClicked(action.currentCity)
        CartScreenAction.GoToCatalogClicked -> navActions.onGoToCatalogClicked()
        is CartScreenAction.ProductClicked -> navActions.onProductClicked(action.product)
    }
}
