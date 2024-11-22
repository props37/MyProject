package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.onboarding.ui.impl.R
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingEvent
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingState
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingStep
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun Onboarding(
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onboardingPage = remember(state) {
        OnboardingPage(
            step = state.currentOnboardingStep,
            number = state.currentStepIndex + 1,
        )
    }

    AnimatedContent(
        targetState = onboardingPage,
        transitionSpec = {
            if (targetState.step == initialState.step) {
                return@AnimatedContent EnterTransition.None togetherWith ExitTransition.None
            }

            val isForward = targetState.number > initialState.number
            val slideInSign = if (isForward) 1 else -1
            val animationSpec = tween<IntOffset>(durationMillis = 300)
            val enter = slideInHorizontally(animationSpec) { it * slideInSign }
            val exit = slideOutHorizontally(animationSpec) { -it * slideInSign }
            (enter togetherWith exit).using(SizeTransform(clip = false))
        },
        label = "Onboarding",
        modifier = modifier,
    ) { page ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            when (page.step) {
                OnboardingStep.NOTIFICATIONS_SETUP -> {
                    NotificationsSetup(
                        onRequestNotificationsPermissionClicked = {
                            onEvent(OnboardingEvent.RequestNotificationsPermissionClicked)
                        },
                    )
                }

                OnboardingStep.CITY_DETECTION -> {
                    CityDetection(
                        onDetectCityClicked = { onEvent(OnboardingEvent.DetectCityClicked) },
                        isDetectCityButtonLoading = state.isDetectCityButtonLoading,
                        onSkipCityDetectionClicked = {
                            onEvent(OnboardingEvent.SkipCityDetectionClicked)
                        },
                        isSkipCityDetectionButtonLoading = state.isSkipCityDetectionButtonLoading,
                    )
                }

                OnboardingStep.CITY_CONFIRMATION -> {
                    CityConfirmation(
                        city = state.city,
                        onConfirmCityClicked = { onEvent(OnboardingEvent.ConfirmCityClicked) },
                        isConfirmCityButtonLoading = state.isConfirmCityButtonLoading,
                        onSelectCityClicked = { onEvent(OnboardingEvent.SelectCityClicked) },
                        onSkipCityDetectionClicked = {
                            onEvent(OnboardingEvent.SkipCityDetectionClicked)
                        },
                        isSkipCityDetectionButtonLoading = state.isSkipCityDetectionButtonLoading,
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationsSetup(
    onRequestNotificationsPermissionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingPageLayout(
        title = stringResource(R.string.onboarding_notifications_setup_title),
        body = stringResource(R.string.onboarding_notifications_setup_body),
        buttons = {
            ZarinaButton(
                onClick = onRequestNotificationsPermissionClicked,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(RCommon.string.continue_).uppercase())
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun CityDetection(
    onDetectCityClicked: () -> Unit,
    isDetectCityButtonLoading: Boolean,
    onSkipCityDetectionClicked: () -> Unit,
    isSkipCityDetectionButtonLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    OnboardingPageLayout(
        title = stringResource(R.string.onboarding_city_selection_title),
        body = stringResource(R.string.onboarding_city_selection_body),
        buttons = {
            ZarinaButton(
                onClick = onDetectCityClicked,
                isLoading = isDetectCityButtonLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.onboarding_detect_city).uppercase())
            }

            Spacer(modifier = Modifier.height(8.dp))

            ZarinaButton(
                onClick = onSkipCityDetectionClicked,
                isLoading = isSkipCityDetectionButtonLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ZarinaButtonDefaults.secondaryColors(),
            ) {
                Text(text = stringResource(RCommon.string.skip).uppercase())
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun CityConfirmation(
    city: City,
    onConfirmCityClicked: () -> Unit,
    isConfirmCityButtonLoading: Boolean,
    onSelectCityClicked: () -> Unit,
    onSkipCityDetectionClicked: () -> Unit,
    isSkipCityDetectionButtonLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    OnboardingPageLayout(
        title = stringResource(R.string.onboarding_city_confirmation_title, city.name),
        body = stringResource(R.string.onboarding_city_confirmation_body),
        buttons = {
            ZarinaButton(
                onClick = onConfirmCityClicked,
                isLoading = isConfirmCityButtonLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.onboarding_yes_correct).uppercase())
            }

            Spacer(modifier = Modifier.height(8.dp))

            ZarinaButton(
                onClick = onSelectCityClicked,
                modifier = Modifier.fillMaxWidth(),
                colors = ZarinaButtonDefaults.outlineColors(),
            ) {
                Text(text = stringResource(R.string.onboarding_no_change).uppercase())
            }

            Spacer(modifier = Modifier.height(8.dp))

            ZarinaButton(
                onClick = onSkipCityDetectionClicked,
                isLoading = isSkipCityDetectionButtonLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ZarinaButtonDefaults.secondaryColors(),
            ) {
                Text(text = stringResource(R.string.onboarding_set_up_later).uppercase())
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun OnboardingPageLayout(
    title: String,
    body: String,
    buttons: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = UiKitTheme.typography.primary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = body,
            style = UiKitTheme.typography.secondary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
        )

        Spacer(modifier = Modifier.height(20.dp))

        buttons()
    }
}

private data class OnboardingPage(
    val step: OnboardingStep,
    val number: Int,
)
