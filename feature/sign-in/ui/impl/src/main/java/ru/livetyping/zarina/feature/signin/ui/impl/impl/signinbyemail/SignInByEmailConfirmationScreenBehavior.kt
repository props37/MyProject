package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

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
internal fun SignInByEmailConfirmationScreenBehavior(
    sideEffects: Flow<SignInByEmailConfirmationSideEffect>,
    navActions: SignInByEmailConfirmationNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SignInByEmailConfirmationSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is SignInByEmailConfirmationSideEffect.ShowZarinaToast -> {
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

private fun navigate(
    navActions: SignInByEmailConfirmationNavActions,
    action: SignInByEmailConfirmationScreenAction
) {
    when (action) {
        SignInByEmailConfirmationScreenAction.BackClicked -> navActions.onBackClicked()
        SignInByEmailConfirmationScreenAction.SignInConfirmed -> navActions.onSignInConfirmed()
    }
}
