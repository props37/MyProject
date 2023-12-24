package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.SideEffect

@Composable
fun OnboardingScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: () -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateForward -> updatedNavigateForward()
            }
        }
    }
}

