package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.ZarinaLinearProgressIndicator
import ru.zarina.zarina.ui.common.component.ZarinaLogo
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModel.OnboardingStep
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.FontFeatureSettings

// TODO: [High] Add previews

object OnboardingScreenComponents {

    // TODO: [High] Use custom ImageLoader to set timeouts
    @Composable
    fun Banner(modifier: Modifier = Modifier) {
        Box(modifier = modifier) {
            val context = LocalContext.current
            val imageRequest = remember(context) {
                ImageRequest.Builder(context)
                    .data(OnboardingViewModel.ONBOARDING_BANNER_URL)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .error(R.drawable.onboarding_default_banner)
                    .build()
            }

            var isBannerDisplayed by remember { mutableStateOf(false) }

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onSuccess = { isBannerDisplayed = true },
                onError = { isBannerDisplayed = true },
                modifier = Modifier.fillMaxSize(),
            )

            val logoColor by animateColorAsState(
                targetValue = if (isBannerDisplayed) {
                    UiKitTheme.colorsReworked.text.general.inversed.default
                } else {
                    UiKitTheme.colorsReworked.text.general.regular.default
                },
                label = "Banner logo color",
            )

            ZarinaLogo(
                color = logoColor,
                animate = !isBannerDisplayed,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(SplashScreenLogoSize),
            )
        }
    }

    @Composable
    fun ProgressIndicator(
        onboardingSteps: List<OnboardingStep>,
        currentOnboardingStep: OnboardingStep,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val currentOnboardingStepNumber = remember(onboardingSteps, currentOnboardingStep) {
                onboardingSteps.indexOf(currentOnboardingStep) + 1
            }
            val onboardingProgress = remember(onboardingSteps, currentOnboardingStepNumber) {
                currentOnboardingStepNumber / onboardingSteps.size.toFloat()
            }

            ZarinaLinearProgressIndicator(
                progress = onboardingProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                val textStyle = UiKitTheme.typographyReworked.tertiary.regular

                Text(
                    text = stringResource(R.string.step),
                    style = textStyle,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
                AnimatedContent(
                    targetState = currentOnboardingStepNumber,
                    transitionSpec = {
                        // Default AnimatedContent transitionSpec without scaling
                        fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90))
                            .togetherWith(fadeOut(animationSpec = tween(durationMillis = 90)))
                    },
                    label = "ProgressIndicator current step number"
                ) { stepNumber ->
                    Text(
                        text = " $stepNumber",
                        style = textStyle.copy(fontFeatureSettings = FontFeatureSettings.Mono),
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                    )
                }
                Text(
                    text = "/${onboardingSteps.size}",
                    style = textStyle.copy(fontFeatureSettings = FontFeatureSettings.Mono),
                    color = UiKitTheme.colorsReworked.text.general.regular.disabled,
                )
            }
        }
    }

    @Composable
    fun OnboardingStep(
        onboardingSteps: List<OnboardingStep>,
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
        modifier: Modifier = Modifier,
    ) {
        val onboardingPage = remember(onboardingSteps, currentOnboardingStep) {
            OnboardingPage(
                step = currentOnboardingStep,
                number = onboardingSteps.indexOf(currentOnboardingStep) + 1,
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
            label = "OnboardingStep",
            modifier = modifier,
        ) { page ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (page.step) {
                    OnboardingStep.NOTIFICATIONS_SETUP -> {
                        OnboardingPageLayout(
                            title = stringResource(R.string.onboarding_notifications_setup_title),
                            body = stringResource(R.string.onboarding_notifications_setup_body),
                            buttons = {
                                ZarinaButton(
                                    onClick = onRequestNotificationsPermissionClicked,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(text = stringResource(R.string.continue_).uppercase())
                                }
                            },
                        )
                    }

                    OnboardingStep.CITY_DETECTION -> {
                        OnboardingPageLayout(
                            title = stringResource(R.string.onboarding_city_selection_title),
                            body = stringResource(R.string.onboarding_city_selection_body),
                            buttons = {
                                ZarinaButton(
                                    onClick = onDetectCityClicked,
                                    isLoading = isDetectCityButtonLoading,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(text = stringResource(R.string.detect_city).uppercase())
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                ZarinaButton(
                                    onClick = onSkipCityDetectionClicked,
                                    isLoading = isSkipCityDetectionButtonLoading,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ZarinaButtonDefaults.secondaryColors(),
                                ) {
                                    Text(text = stringResource(R.string.skip).uppercase())
                                }
                            },
                        )
                    }

                    OnboardingStep.CITY_CONFIRMATION -> {
                        OnboardingPageLayout(
                            title = stringResource(
                                R.string.onboarding_city_confirmation_title,
                                userCity?.name.orEmpty(),
                            ),
                            body = stringResource(R.string.onboarding_city_confirmation_body),
                            buttons = {
                                ZarinaButton(
                                    onClick = onConfirmCityClicked,
                                    isLoading = isConfirmCityButtonLoading,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(text = stringResource(R.string.yes_correct).uppercase())
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                ZarinaButton(
                                    onClick = onSelectCityClicked,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ZarinaButtonDefaults.outlineColors(),
                                ) {
                                    Text(text = stringResource(R.string.no_change).uppercase())
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                ZarinaButton(
                                    onClick = onSkipCityDetectionClicked,
                                    isLoading = isSkipCityDetectionButtonLoading,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ZarinaButtonDefaults.secondaryColors(),
                                ) {
                                    Text(text = stringResource(R.string.configure_later).uppercase())
                                }
                            },
                        )
                    }
                }
            }
        }
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
                style = UiKitTheme.typographyReworked.primary.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = body,
                style = UiKitTheme.typographyReworked.secondary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
            )

            Spacer(modifier = Modifier.height(20.dp))

            buttons()
        }
    }

    private data class OnboardingPage(
        val step: OnboardingStep,
        val number: Int,
    )

    // According to https://developer.android.com/develop/ui/views/launch/splash-screen
    private val SplashScreenLogoSize = 192.dp
}
