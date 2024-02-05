package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.ZarinaBottomSheet
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenComponents.Banner
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenComponents.OnboardingStep
import ru.zarina.zarina.ui.screen.onboarding.OnboardingScreenComponents.ProgressIndicator
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.OnboardingStep
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.SideEffect
import ru.zarina.zarina.ui.screen.onboarding.tooling.preview.OnboardingStepPreviewParameterProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.HorizontalAndBottom

@Composable
fun OnboardingScreen(
    navigateForward: (OnboardingScreenAction) -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val bannerUrl by viewModel.bannerUrl.collectAsStateWithLifecycle()
    val onboardingSteps by viewModel.onboardingSteps.collectAsStateWithLifecycle()
    val currentOnboardingStep by viewModel.currentOnboardingStep.collectAsStateWithLifecycle()
    val userCity by viewModel.userCity.collectAsStateWithLifecycle()
    val isSkipCityDetectionButtonLoading by viewModel.isSkipCityDetectionButtonLoading.collectAsStateWithLifecycle()
    val isDetectCityButtonLoading by viewModel.isDetectCityButtonLoading.collectAsStateWithLifecycle()
    val isConfirmCityButtonLoading by viewModel.isConfirmCityButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        bannerUrl = bannerUrl,
        onboardingSteps = onboardingSteps,
        currentOnboardingStep = currentOnboardingStep,
        userCity = userCity,
        isSkipCityDetectionButtonLoading = isSkipCityDetectionButtonLoading,
        isDetectCityButtonLoading = isDetectCityButtonLoading,
        isConfirmCityButtonLoading = isConfirmCityButtonLoading,
        onRequestNotificationsPermissionClicked = viewModel::onRequestNotificationsPermissionClicked,
        onDetectCityClicked = viewModel::onDetectCityClicked,
        onSkipCityDetectionClicked = viewModel::onSkipCityDetectionClicked,
        onConfirmCityClicked = viewModel::onConfirmCityClicked,
        onSelectCityClicked = viewModel::onSelectCityClicked,
        sideEffects = viewModel.sideEffects,
        navigateForward = navigateForward,
    )
}

@Composable
private fun ScreenContent(
    bannerUrl: Url?,
    onboardingSteps: ImmutableList<OnboardingStep>,
    currentOnboardingStep: OnboardingStep,
    userCity: City?,
    isSkipCityDetectionButtonLoading: Boolean,
    isDetectCityButtonLoading: Boolean,
    isConfirmCityButtonLoading: Boolean,
    onRequestNotificationsPermissionClicked: () -> Unit,
    onDetectCityClicked: () -> Unit,
    onSkipCityDetectionClicked: () -> Unit,
    onConfirmCityClicked: () -> Unit,
    onSelectCityClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateForward: (OnboardingScreenAction) -> Unit,
) {
    OnboardingScreenBehavior(
        sideEffects = sideEffects,
        navigateForward = navigateForward,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default),
    ) {
        Banner(
            url = bannerUrl,
            modifier = Modifier.fillMaxSize(),
        )

        ZarinaBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.navigationBars
                            .union(WindowInsets.displayCutout)
                            .only(WindowInsetsSides.HorizontalAndBottom),
                    )
                    .padding(top = 24.dp, bottom = 16.dp),
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
                    userCity = userCity,
                    isSkipCityDetectionButtonLoading = isSkipCityDetectionButtonLoading,
                    isDetectCityButtonLoading = isDetectCityButtonLoading,
                    isConfirmCityButtonLoading = isConfirmCityButtonLoading,
                    onRequestNotificationsPermissionClicked = onRequestNotificationsPermissionClicked,
                    onDetectCityClicked = onDetectCityClicked,
                    onSkipCityDetectionClicked = onSkipCityDetectionClicked,
                    onConfirmCityClicked = onConfirmCityClicked,
                    onSelectCityClicked = onSelectCityClicked,
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
private fun Preview(
    @PreviewParameter(OnboardingStepPreviewParameterProvider::class)
    onboardingStep: OnboardingStep,
) {
    ZarinaPreview {
        ScreenContent(
            bannerUrl = null,
            onboardingSteps = remember { OnboardingStep.entries.toImmutableList() },
            currentOnboardingStep = onboardingStep,
            userCity = remember { City.DEFAULT },
            isSkipCityDetectionButtonLoading = false,
            isDetectCityButtonLoading = false,
            isConfirmCityButtonLoading = false,
            onRequestNotificationsPermissionClicked = {},
            onDetectCityClicked = {},
            onSkipCityDetectionClicked = {},
            onConfirmCityClicked = {},
            onSelectCityClicked = {},
            sideEffects = remember { emptyFlow() },
            navigateForward = {},
        )
    }
}
