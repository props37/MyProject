package ru.livetyping.zarina.presentation.screen.onboarding

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.BottomNavBarBehaviorController
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.livetyping.zarina.presentation.common.navigation.safeNavigate
import ru.livetyping.zarina.presentation.common.toastcontroller.LocalToastController
import ru.livetyping.zarina.presentation.screen.onboarding.OnboardingViewModel.SideEffect

@Composable
fun OnboardingScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (OnboardingScreenAction) -> Unit,
) {
    val updatedToastController by rememberUpdatedState(LocalToastController.current)
    val updatedBottomNavBarController by rememberUpdatedState(LocalBottomNavBarBehaviorController.current)
    val updatedNavigateForward by rememberUpdatedState(navigateForward)

    ForcedBottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val startedElapsedRealtime = SystemClock.elapsedRealtime()
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SideEffect.NavigateForward -> {
                        safeNavigate(startedElapsedRealtime) {
                            if (sideEffect.action is OnboardingScreenAction.OnboardingCompleted) {
                                makeBottomNavBarVisibleByDefault(updatedBottomNavBarController)
                            }

                            updatedNavigateForward(sideEffect.action)
                        }
                    }

                    is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun makeBottomNavBarVisibleByDefault(controller: BottomNavBarBehaviorController) {
    val defaultBehavior = BottomNavBarBehavior.Visible(isAnimated = false)
    controller.setDefaultBehavior(defaultBehavior)
}
