package ru.livetyping.zarina.feature.webview.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature

@Composable
internal fun WebViewScreenBehavior(
    sideEffects: Flow<WebViewSideEffect>,
    navActions: WebViewFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is WebViewSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: WebViewFeature.NavActions, action: WebViewScreenAction) {
    when (action) {
        WebViewScreenAction.BackClicked -> navActions.onBackClicked()
    }
}
