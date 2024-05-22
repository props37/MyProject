package ru.livetyping.zarina.presentation.screen.productsearch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.screen.productsearch.ProductSearchViewModel.SideEffect

@Composable
fun ProductSearchScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSearchScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                        SideEffect.ReleaseSearchTextFieldFocus -> updatedFocusManager.clearFocus()
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
