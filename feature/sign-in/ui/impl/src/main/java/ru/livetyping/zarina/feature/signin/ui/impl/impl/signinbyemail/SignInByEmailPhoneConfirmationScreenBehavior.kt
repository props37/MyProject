package ru.livetyping.zarina.feature.signin.ui.impl.impl.signinbyemail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun SignInByEmailPhoneConfirmationScreenBehavior(
    sideEffects: Flow<SignInByEmailPhoneConfirmationSideEffect>,
    navActions: SignInByEmailPhoneConfirmationNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SignInByEmailPhoneConfirmationSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is SignInByEmailPhoneConfirmationSideEffect.ShowZarinaToast -> {
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
    navActions: SignInByEmailPhoneConfirmationNavActions,
    action: SignInByEmailPhoneConfirmationScreenAction
) {
    // TODO: [Top] Implement
    TODO()
}
