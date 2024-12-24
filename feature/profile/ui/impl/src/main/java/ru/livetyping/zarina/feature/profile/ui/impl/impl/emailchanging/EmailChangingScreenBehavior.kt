package ru.livetyping.zarina.feature.profile.ui.impl.impl.emailchanging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun EmailChangingScreenBehavior(
    sideEffects: Flow<EmailChangingSideEffect>,
    navActions: EmailChangingNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is EmailChangingSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is EmailChangingSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: EmailChangingNavActions, action: EmailChangingScreenAction) {
    when (action) {
        EmailChangingScreenAction.BackClicked -> navActions.onBackClicked()
        EmailChangingScreenAction.EmailChanged -> navActions.onEmailChanged()
    }
}
