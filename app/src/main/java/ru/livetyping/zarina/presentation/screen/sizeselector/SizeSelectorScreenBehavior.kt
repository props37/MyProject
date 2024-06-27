package ru.livetyping.zarina.presentation.screen.sizeselector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.screen.sizeselector.SizeSelectorViewModel.SideEffect

@Composable
fun SizeSelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (SizeSelectorScreenAction) -> Unit,
) {
    val updatedNavigate by rememberUpdatedState(navigate)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
