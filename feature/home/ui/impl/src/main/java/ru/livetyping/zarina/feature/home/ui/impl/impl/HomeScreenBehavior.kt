package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.home.ui.HomeFeature

@Composable
internal fun HomeScreenBehavior(
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<HomeSideEffect>,
    navActions: HomeFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(onLifecycleEvent = onLifecycleEvent)

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
