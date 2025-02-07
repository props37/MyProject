package ru.livetyping.zarina.presentation.screen.productsearch

import android.os.SystemClock
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
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SideEffect

@Composable
fun ProductSearchScreenBehavior(
    onScreenCreated: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSearchScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        onScreenCreated()
    }

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> {
                        updatedKeyboardController?.hide()
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigate(sideEffect.action)
                        }
                    }

                    SideEffect.ReleaseSearchTextFieldFocus -> updatedFocusManager.clearFocus()
                    is SideEffect.ShowZarinaToast -> {
                        updatedZarinaToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
