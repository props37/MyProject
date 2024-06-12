package ru.livetyping.zarina.presentation.screen.loyaltyprogram

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
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.screen.loyaltyprogram.LoyaltyProgramViewModel.SideEffect

@Composable
fun LoyaltyProgramScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (LoyaltyProgramScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

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
                            val uri = sideEffect.url.getString(updatedContext).toUri()
                            intent.launchUrl(updatedContext, uri)
                        }
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
