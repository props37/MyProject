package ru.zarina.zarina.ui.screen.defaultcitydialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogViewModel.SideEffect

@Composable
fun DefaultCityDialogScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (DefaultCityDialogScreenResult) -> Unit,
) {
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
