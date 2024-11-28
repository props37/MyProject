package ru.livetyping.zarina.feature.wishlist.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.navigationutil.LifecycleSafeNavigator
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController
import ru.livetyping.zarina.feature.wishlist.ui.WishlistNavActions

@Composable
internal fun WishlistScreenBehavior(
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<WishlistSideEffect>,
    navActions: WishlistNavActions,
) {
    val currentOnLifecycleEvent by rememberUpdatedState(onLifecycleEvent)
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(onLifecycleEvent = currentOnLifecycleEvent)

    LifecycleStartEffect(sideEffects) {
        val lifecycleSafeNavigator = LifecycleSafeNavigator()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is WishlistSideEffect.Navigate -> {
                        lifecycleSafeNavigator.safeNavigate {
                            navigate(currentNavActions, sideEffect.action)
                        }
                    }

                    is WishlistSideEffect.ShowZarinaToast -> {
                        currentZarinaToastController.show(sideEffect.message)
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
    when (action) {
        WishlistScreenAction.GoToCatalogClicked -> navActions.onGoToCatalogClicked()
    }
}
