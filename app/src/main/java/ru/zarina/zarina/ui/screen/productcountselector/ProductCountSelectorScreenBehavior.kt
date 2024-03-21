package ru.zarina.zarina.ui.screen.productcountselector

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.screen.productcountselector.ProductCountSelectorViewModel.SideEffect

@Composable
fun ProductCountSelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
) {
    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->

                }
            }
        }

        onStopOrDispose {}
    }
}
