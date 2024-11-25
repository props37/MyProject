package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile

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
    sideEffects: Flow<ProfileSideEffect>,
    navActions: ProfileNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentContext by rememberUpdatedState(LocalContext.current)

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
        ProfileScreenAction.SignInClicked -> navActions.onSignInClicked()
        is ProfileScreenAction.ChangeCityClicked -> {
            navActions.onChangeCityClicked(action.currentCity)
        }
    }
}
