package ru.zarina.zarina.ui.screen.home

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
import ru.zarina.zarina.ui.screen.home.HomeViewModel.SideEffect

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (HomeScreenAction) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.NavigateForward -> updatedNavigateForward(sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}

