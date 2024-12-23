package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
internal fun ProfileDetailsScreenBehavior(
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<ProfileDetailsSideEffect>,
    navActions: ProfileDetailsNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(onLifecycleEvent = onLifecycleEvent)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProfileDetailsSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is ProfileDetailsSideEffect.ShowZarinaToast -> {
                        currentZarinaToastController.show(sideEffect.message)
                    }

                    ProfileDetailsSideEffect.HideKeyboard -> currentKeyboardController?.hide()
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: ProfileDetailsNavActions, action: ProfileDetailsScreenAction) {
    when (action) {
        ProfileDetailsScreenAction.BackClicked -> navActions.onBackClicked()
        ProfileDetailsScreenAction.ChangePhoneClicked -> navActions.onChangePhoneClicked()
        ProfileDetailsScreenAction.ChangeEmailClicked -> navActions.onChangeEmailClicked()
        ProfileDetailsScreenAction.UserSignedOut -> navActions.onUserSignedOut()
        ProfileDetailsScreenAction.AccountDeleted -> navActions.onAccountDeleted()
    }
}
