package ru.livetyping.zarina.feature.home.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.ui.kit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.home.ui.HomeNavActions

@Composable
internal fun HomeScreenBehavior(
    sideEffects: Flow<HomeScreenSideEffect>,
    navActions: HomeNavActions,
) {
    val updatedNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is HomeScreenSideEffect.Navigate -> {
                        navigate(updatedNavActions, sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: HomeNavActions, action: HomeScreenAction) {
    when (action) {
        is HomeScreenAction.BannerClicked -> {
            navActions.bannerClicked(action.banner)
        }
    }
}
