package ru.livetyping.zarina.feature.detectedcity.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.feature.detectedcity.ui.DetectedCityFeature

@Composable
internal fun DetectedCityScreenBehavior(
    sideEffects: Flow<DetectedCitySideEffect>,
    navActions: DetectedCityFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is DetectedCitySideEffect.Navigate -> {
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

private fun navigate(navActions: DetectedCityFeature.NavActions, action: DetectedCityScreenAction) {
    when (action) {
        DetectedCityScreenAction.CloseClicked -> navActions.onCloseClicked()
    }
}
