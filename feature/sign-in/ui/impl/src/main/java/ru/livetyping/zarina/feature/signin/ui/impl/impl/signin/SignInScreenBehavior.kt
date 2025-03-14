package ru.livetyping.zarina.feature.signin.ui.impl.impl.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun SignInScreenBehavior(
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<SignInSideEffect>,
    navActions: SignInNavActions,
) {
    val currentOnLifecycleEvent by rememberUpdatedState(onLifecycleEvent)
    val currentNavActions by rememberUpdatedState(navActions)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val currentFocusManager by rememberUpdatedState(LocalFocusManager.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleEventEffect(onLifecycleEvent = currentOnLifecycleEvent)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SignInSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    SignInSideEffect.FreeFocus -> currentFocusManager.clearFocus()
                    is SignInSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: SignInNavActions, action: SignInScreenAction) {
    when (action) {
        SignInScreenAction.BackClicked -> navActions.onBackClicked()
        SignInScreenAction.UserSignedIn -> navActions.onUserSignedIn()
        is SignInScreenAction.SignInByPhoneRequested -> {
            navActions.onSignInByPhoneRequested(action.phone)
        }

        SignInScreenAction.ForgotPasswordClicked -> navActions.onForgotPasswordClicked()
        SignInScreenAction.SignUpClicked -> navActions.onSignUpClicked()
        is SignInScreenAction.PhoneConfirmationNeeded -> {
            navActions.onSignInByEmailPhoneConfirmationNeeded(action.phone)
        }
    }
}
