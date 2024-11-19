package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.navigationutil.LifecycleSafeNavigator
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions

@Composable
internal fun CitySelectorScreenBehavior(
    sideEffects: Flow<CitySelectorSideEffect>,
    navActions: CitySelectorNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val lifecycleSafeNavigator = LifecycleSafeNavigator()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CitySelectorSideEffect.Navigate -> {
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

private fun navigate(navActions: CitySelectorNavActions, action: CitySelectorScreenAction) {
    when (action) {
        CitySelectorScreenAction.ScreenClosed -> navActions.backClicked()
    }
}
