package ru.zarina.zarina.ui.screen.catalog

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
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.SideEffect

@Composable
fun CatalogScreenBehavior(
    sideEffects: Flow<SideEffect>,
) {
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)

    ForcedBottomNavBarBehavior(isVisible = true)

    // TODO: [Top] Test
    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        SideEffect.FreeSearchBarFocus -> updatedFocusManager.clearFocus(force = true)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}

