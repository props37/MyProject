package ru.livetyping.zarina.ui.screen.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.ui.common.toastcontroller.LocalToastController
import ru.livetyping.zarina.ui.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.ui.screen.cart.CartViewModel.SideEffect

@Composable
fun CartScreenBehavior(
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CartScreenAction) -> Unit,
) {
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedToastController by rememberUpdatedState(LocalToastController.current)
    val updatedOnScreenOpened by rememberUpdatedState(onScreenOpened)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

    // TODO: [High] Consider refactoring
    LifecycleStartEffect(Unit) {
        updatedOnScreenOpened()
        onStopOrDispose {}
    }

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                        is SideEffect.ShowZarinaToast -> {
                            updatedZarinaToastController.show(sideEffect.message)
                        }

                        is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
