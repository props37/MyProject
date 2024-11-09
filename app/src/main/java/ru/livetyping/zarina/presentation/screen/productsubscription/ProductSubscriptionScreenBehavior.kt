package ru.livetyping.zarina.presentation.screen.productsubscription

import android.os.SystemClock
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.productsubscription.ProductSubscriptionViewModel.SideEffect

@Composable
fun ProductSubscriptionScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSubscriptionScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

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

                    is SideEffect.OpenUrl -> {
                        val intent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .build()
                        intent.launchUrl(updatedContext, sideEffect.url.value.toUri())
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
