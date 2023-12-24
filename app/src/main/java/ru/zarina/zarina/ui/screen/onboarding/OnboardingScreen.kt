package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenComponents.Background
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenComponents.OnboardingStep
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenComponents.ProgressIndicator
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.OnboardingStep
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.SideEffect
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme
import ru.zarina.zarina.util.compose.HorizontalAndBottom

@Composable
fun OnboardingScreen(
    navigateForward: (OnboardingScreenAction) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val onboardingSteps by viewModel.onboardingSteps.collectAsStateWithLifecycle()
    val currentOnboardingStep by viewModel.currentOnboardingStep.collectAsStateWithLifecycle()
    val currentCity by viewModel.currentCity.collectAsStateWithLifecycle()
    val isDetectCityButtonLoading by viewModel.isDetectCityButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        onboardingSteps = onboardingSteps,
        currentOnboardingStep = currentOnboardingStep,
        currentCity = currentCity,
        isDetectCityButtonLoading = isDetectCityButtonLoading,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
        onRequestNotificationsPermissionClicked = viewModel::onRequestNotificationsPermissionClicked,
        onDetectCityClicked = viewModel::onDetectCityClicked,
        onSkipCityDetectionClicked = viewModel::onSkipCityDetectionClicked,
    )
}

@Composable
private fun ScreenContent(
    onboardingSteps: List<OnboardingStep>,
    currentOnboardingStep: OnboardingStep,
    currentCity: City?,
    isDetectCityButtonLoading: Boolean,
    sideEffects: Flow<SideEffect>,
    navigateForward: (OnboardingScreenAction) -> Unit,
    onRequestNotificationsPermissionClicked: () -> Unit,
    onDetectCityClicked: () -> Unit,
    onSkipCityDetectionClicked: () -> Unit,
) {
    OnboardingScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Background(modifier = Modifier.fillMaxSize())

        ZarinaBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.HorizontalAndBottom)
                    )
                    .padding(top = 24.dp, bottom = 20.dp),
            ) {
                ProgressIndicator(
                    onboardingSteps = onboardingSteps,
                    currentOnboardingStep = currentOnboardingStep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OnboardingStep(
                    onboardingSteps = onboardingSteps,
                    currentOnboardingStep = currentOnboardingStep,
                    currentCity = currentCity,
                    isDetectCityButtonLoading = isDetectCityButtonLoading,
                    onRequestNotificationsPermissionClicked = onRequestNotificationsPermissionClicked,
                    onDetectCityClicked = onDetectCityClicked,
                    onSkipCityDetectionClicked = onSkipCityDetectionClicked,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaTheme {
        // TODO: [High] Add preview
    }
}
