package ru.zarina.zarina.ui.screen.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.SideEffect

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (FiltersScreenAction) -> Unit,
    navigateBackward: (FiltersScreenResult) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.NavigateForward -> updatedNavigateForward(sideEffect.action)
                        is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
