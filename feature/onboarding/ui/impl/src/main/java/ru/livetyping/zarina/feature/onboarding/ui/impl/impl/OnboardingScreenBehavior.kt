package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.navigationutil.safeNavigate
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions

@Composable
internal fun OnboardingScreenBehavior(
    sideEffects: Flow<OnboardingSideEffect>,
    navActions: OnboardingNavActions,
) {
    val updatedNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is OnboardingSideEffect.Navigate -> {
                        safeNavigate(startedElapsedRealtime) {
                            navigate(updatedNavActions, sideEffect.action)
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

private fun navigate(navActions: OnboardingNavActions, action: OnboardingScreenAction) {
    // TODO: [Top] Implement
    TODO()
}
