package ru.livetyping.zarina.presentation.screen.sizeselector.heightselector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.screen.sizeselector.heightselector.HeightSelectorViewModel.SideEffect

@Composable
fun HeightSelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (HeightSelectorScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
