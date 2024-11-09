package ru.livetyping.zarina.presentation.screen.productsearch.filters

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.screen.productsearch.filters.ProductSearchFiltersViewModel.SideEffect

@Composable
fun ProductSearchFiltersScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (ProductSearchFiltersScreenAction) -> Unit,
    navigateBackward: (ProductSearchFiltersScreenResult) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.NavigateForward -> {
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigateForward(sideEffect.action)
                        }
                    }

                    is SideEffect.NavigateBackward -> {
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigateBackward(sideEffect.result)
                        }
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
