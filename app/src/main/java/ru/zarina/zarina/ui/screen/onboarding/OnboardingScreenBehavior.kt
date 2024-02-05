package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.LocalToastController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.BottomNavBarBehaviorController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.LocalBottomNavBarBehaviorController
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

    LaunchedEffect(sideEffects) {
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

private fun makeBottomNavBarVisibleByDefault(controller: BottomNavBarBehaviorController) {
    val defaultBehavior = BottomNavBarBehavior.Visible(isAnimated = false)
    controller.setDefaultBehavior(defaultBehavior)
}
