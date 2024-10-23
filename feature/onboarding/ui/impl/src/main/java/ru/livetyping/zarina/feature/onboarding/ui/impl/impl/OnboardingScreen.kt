package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions

@Composable
internal fun OnboardingScreen(
    navActions: OnboardingNavActions,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    ScreenContent(
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    sideEffects: Flow<OnboardingSideEffect>,
    navActions: OnboardingNavActions,
) {
    OnboardingScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default),
    ) {

    }
}
