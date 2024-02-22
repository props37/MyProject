package ru.zarina.zarina.ui.screen.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.common.LocalToastController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect

@Composable
fun ProductsScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (ProductsScreenAction) -> Unit,
    navigateBackward: () -> Unit,
) {
    val updatedToastController by rememberUpdatedState(LocalToastController.current)
    val updatedNavigateForward by rememberUpdatedState(navigateForward)
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.NavigateForward -> updatedNavigateForward(sideEffect.action)
                        SideEffect.NavigateBackward -> updatedNavigateBackward()
                        is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
