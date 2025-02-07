package ru.livetyping.zarina.presentation.screen.product

import android.os.SystemClock
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.product.ProductViewModel.SideEffect
import ru.livetyping.zarina.util.domain.common.toUri
import ru.livetyping.zarina.util.platform.shareText

@Composable
fun ProductScreenBehavior(
    onScreenCreated: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        onScreenCreated()
    }

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.Navigate -> {
                        safeNavigate(startedElapsedRealtime) {
                            updatedNavigate(sideEffect.action)
                        }
                    }

                    is SideEffect.Share -> updatedContext.shareText(sideEffect.text)
                    is SideEffect.OpenUrl -> {
                        val intent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .build()
                        intent.launchUrl(updatedContext, sideEffect.url.toUri())
                    }

                    is SideEffect.ShowZarinaToast -> {
                        updatedZarinaToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}
