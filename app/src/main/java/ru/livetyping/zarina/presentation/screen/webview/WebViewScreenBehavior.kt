package ru.livetyping.zarina.presentation.screen.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.screen.webview.WebViewViewModel.SideEffect

@Composable
fun WebViewScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (WebViewScreenAction) -> Unit,
) {
    val currentNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> {
                        currentNavigate(sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
