package ru.livetyping.zarina.ui.screen.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.ui.common.toastcontroller.LocalToastController
import ru.livetyping.zarina.ui.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect

@Composable
fun FavoritesScreenBehavior(
    onScreenCreated: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (FavoritesScreenAction) -> Unit,
) {
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedToastController by rememberUpdatedState(LocalToastController.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        onScreenCreated()
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
