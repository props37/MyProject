package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.navigationutil.LifecycleSafeNavigator
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions

@Composable
internal fun OnboardingScreenBehavior(
    sideEffects: Flow<OnboardingSideEffect>,
    navActions: OnboardingNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val lifecycleSafeNavigator = LifecycleSafeNavigator()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is OnboardingSideEffect.Navigate -> {
                        lifecycleSafeNavigator.safeNavigate {
                            navigate(currentNavActions, sideEffect.action)
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
