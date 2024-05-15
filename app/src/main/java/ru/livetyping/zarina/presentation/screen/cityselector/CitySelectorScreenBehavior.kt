package ru.livetyping.zarina.presentation.screen.cityselector

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
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.SideEffect

@Composable
fun CitySelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (CitySelectorScreenAction) -> Unit,
) {
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> {
                            updatedFocusManager.clearFocus(force = true)
                            updatedNavigate(sideEffect.action)
                        }

                        SideEffect.FreeCitySearchBarFocus -> {
                            updatedFocusManager.clearFocus(force = true)
                        }
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
