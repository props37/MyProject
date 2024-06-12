package ru.livetyping.zarina.presentation.screen.filters.listfilter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.screen.filters.listfilter.ListFilterViewModel.SideEffect

@Composable
fun ListFilterScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (ListFilterScreenResult) -> Unit,
) {
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
