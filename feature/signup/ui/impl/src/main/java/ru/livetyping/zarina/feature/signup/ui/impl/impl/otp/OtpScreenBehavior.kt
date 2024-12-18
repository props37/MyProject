package ru.livetyping.zarina.feature.signup.ui.impl.impl.otp

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
internal fun OtpScreenBehavior(
    sideEffects: Flow<OtpSideEffect>,
    navActions: OtpNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is OtpSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is OtpSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: OtpNavActions, action: OtpScreenAction) {
    when (action) {
        OtpScreenAction.BackClicked -> navActions.onBackClicked()
        OtpScreenAction.SignUpConfirmed -> navActions.onSignUpConfirmed()
    }
}
