package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.toastcontroller.LocalToastController
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.SideEffect

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
                        is SideEffect.NavigateForward -> {
                            if (sideEffect.action is OnboardingScreenAction.OnboardingCompleted) {
                                makeBottomNavBarVisibleByDefault(updatedBottomNavBarController)
                            }

                            updatedNavigateForward(sideEffect.action)
                        }
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}

private fun makeBottomNavBarVisibleByDefault(controller: BottomNavBarBehaviorController) {
    val defaultBehavior = BottomNavBarBehavior.Visible(isAnimated = false)
    controller.setDefaultBehavior(defaultBehavior)
}
