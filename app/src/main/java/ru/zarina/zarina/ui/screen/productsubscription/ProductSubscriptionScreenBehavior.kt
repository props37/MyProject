package ru.zarina.zarina.ui.screen.productsubscription

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.common.LocalToastController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionViewModel.SideEffect

@Composable
fun ProductSubscriptionScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSubscriptionScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedToastController by rememberUpdatedState(LocalToastController.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                        is SideEffect.OpenUrl -> {
                            val intent = CustomTabsIntent.Builder()
                                .setShowTitle(true)
                                .build()
                            intent.launchUrl(updatedContext, sideEffect.url.value.toUri())
                        }

                        is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
