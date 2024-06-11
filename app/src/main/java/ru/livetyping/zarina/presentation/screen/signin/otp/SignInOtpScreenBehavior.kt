package ru.livetyping.zarina.presentation.screen.signin.otp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.signin.otp.SignInOtpViewModel.SideEffect

@Composable
fun SignInOtpScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (SignInOtpScreenAction) -> Unit,
) {
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedNavigate by rememberUpdatedState(navigate)
    val updatedKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        SideEffect.HideKeyboard -> {
                            updatedKeyboardController?.hide()
                        }

                        is SideEffect.Navigate -> {
                            updatedNavigate(sideEffect.action)
                            updatedKeyboardController?.hide()
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
