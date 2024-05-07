package ru.livetyping.zarina.presentation.screen.profile

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.profile.ProfileViewModel.SideEffect
import ru.livetyping.zarina.util.domain.common.toUri

@Composable
fun ProfileScreenBehavior(
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProfileScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedOnScreenOpened by rememberUpdatedState(onScreenOpened)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect {
        updatedOnScreenOpened()
        onStopOrDispose {}
    }

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
                            intent.launchUrl(updatedContext, sideEffect.url.toUri())
                        }

                        is SideEffect.ShowZarinaToast -> {
                            updatedZarinaToastController.show(sideEffect.message)
                        }
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}
