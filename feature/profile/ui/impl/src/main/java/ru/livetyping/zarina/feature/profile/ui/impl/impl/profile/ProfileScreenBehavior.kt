package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.navigationutil.LifecycleSafeNavigator
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior

@Composable
internal fun ProfileScreenBehavior(
    onBackClicked: () -> Unit,
    sideEffects: Flow<ProfileSideEffect>,
    navActions: ProfileNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentContext by rememberUpdatedState(LocalContext.current)

    BackHandler(onBack = onBackClicked)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val lifecycleSafeNavigator = LifecycleSafeNavigator()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ProfileSideEffect.Navigate -> {
                        lifecycleSafeNavigator.safeNavigate {
                            navigate(currentNavActions, sideEffect.action)
                        }
                    }

                    is ProfileSideEffect.OpenUrl -> {
                        val url = sideEffect.url.getString(currentContext)
                        currentContext.openUrlInCustomTabs(url)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: ProfileNavActions, action: ProfileScreenAction) {
    when (action) {
        ProfileScreenAction.BackClicked -> navActions.onBackClicked()
        ProfileScreenAction.SignInClicked -> navActions.onSignInClicked()
        ProfileScreenAction.SignUpClicked -> navActions.onSignUpClicked()
        ProfileScreenAction.ProfileDetailsClicked -> navActions.onProfileDetailsClicked()
        ProfileScreenAction.MyOrdersClicked -> navActions.onMyOrdersClicked()
        is ProfileScreenAction.ChangeCityClicked -> {
            navActions.onChangeCityClicked(action.currentCity)
        }
    }
}
