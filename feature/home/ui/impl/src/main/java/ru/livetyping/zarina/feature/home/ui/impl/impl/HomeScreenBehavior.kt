package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.home.ui.HomeFeature

@Composable
internal fun HomeScreenBehavior(
    sideEffects: Flow<HomeSideEffect>,
    navActions: HomeFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is HomeSideEffect.Navigate -> navigate(currentNavActions, sideEffect.action)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: HomeFeature.NavActions, action: HomeScreenAction) {
    when (action) {
        is HomeScreenAction.BannerClicked -> {
            navActions.onBannerClicked(action.banner)
        }
    }
}
