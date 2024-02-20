package ru.zarina.zarina.ui.screen.cityselector

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
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.SideEffect

@Composable
fun CitySelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (CitySelectorScreenResult) -> Unit,
) {
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)

    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.NavigateBackward -> {
                            updatedFocusManager.clearFocus(force = true)
                            updatedNavigateBackward(sideEffect.result)
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
