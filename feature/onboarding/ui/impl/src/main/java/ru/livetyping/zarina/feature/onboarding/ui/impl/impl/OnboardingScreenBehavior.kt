package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import cloud.mindbox.mobile_sdk.Mindbox
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeature

@Composable
internal fun OnboardingScreenBehavior(
    sideEffects: Flow<OnboardingSideEffect>,
    navActions: OnboardingFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentContext by rememberUpdatedState(LocalContext.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is OnboardingSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    OnboardingSideEffect.NotificationPermissionGranted -> {
                        Mindbox.updateNotificationPermissionStatus(currentContext)
                    }

                    is OnboardingSideEffect.ShowToast -> {
                        Toast.makeText(
                            /* context = */ currentContext,
                            /* text = */ sideEffect.text.getString(currentContext),
                            /* duration = */ Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: OnboardingFeature.NavActions, action: OnboardingScreenAction) {
    when (action) {
        is OnboardingScreenAction.OnboardingCompleted -> {
            navActions.onOnboardingCompleted(action.selectedCity)
        }

        is OnboardingScreenAction.SelectCityClicked -> navActions.onSelectCityClicked()
    }
}
