package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

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
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions

@Composable
internal fun WishlistScreenBehavior(
    onScreenOpened: () -> Unit,
    sideEffects: Flow<WishlistSideEffect>,
    navActions: WishlistNavActions,
) {
    val updatedOnScreenOpened by rememberUpdatedState(onScreenOpened)
    val updatedNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(Unit) {
        updatedOnScreenOpened()
        onStopOrDispose {}
    }

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is WishlistSideEffect.Navigate -> {
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

private fun navigate(navActions: WishlistNavActions, action: WishlistScreenAction) {
    // TODO: [Top] Implement
    TODO()
}
