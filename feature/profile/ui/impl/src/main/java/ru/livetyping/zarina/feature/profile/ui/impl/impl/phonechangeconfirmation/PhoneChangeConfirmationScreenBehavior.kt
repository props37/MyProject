package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechangeconfirmation

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
internal fun PhoneChangeConfirmationScreenBehavior(
    sideEffects: Flow<PhoneChangeConfirmationSideEffect>,
    navActions: PhoneChangeConfirmationNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PhoneChangeConfirmationSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is PhoneChangeConfirmationSideEffect.ShowZarinaToast -> {
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
    navActions: PhoneChangeConfirmationNavActions,
    action: PhoneChangeConfirmationScreenAction,
) {
    when (action) {
        PhoneChangeConfirmationScreenAction.BackClicked -> navActions.onBackClicked()
        PhoneChangeConfirmationScreenAction.PhoneChangeConfirmed -> {
            navActions.onPhoneChangeConfirmed()
        }
    }
}
