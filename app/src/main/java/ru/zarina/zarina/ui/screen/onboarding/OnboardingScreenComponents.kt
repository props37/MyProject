package ru.zarina.zarina.ui.screen.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.OnboardingStep
import ru.zarina.zarina.ui.common.component.ZarinaLinearProgressIndicator
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.screen.onboarding.OnboardingViewModelRework.Onboarding
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.FontFeatureSettings

object OnboardingScreenComponents {

    @Composable
    fun Background(
        modifier: Modifier = Modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.onboarding_default_banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    }

    @Composable
    fun ProgressIndicator(
        onboarding: Onboarding,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val onboardingProgress = remember(onboarding) {
                onboarding.currentPage.number / onboarding.pageCount.toFloat()
            }

            ZarinaLinearProgressIndicator(
                progress = onboardingProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                val textStyle = UiKitTheme.typographyReworked.tertiaryText.regular

                Text(
                    text = stringResource(R.string.step),
                    style = textStyle,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
                AnimatedContent(
                    targetState = onboarding.currentPage.number,
                    transitionSpec = {
                        // Default AnimatedContent transitionSpec without scaling
                        fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90))
                            .togetherWith(fadeOut(animationSpec = tween(durationMillis = 90)))
                    },
                    label = "ProgressIndicator current page number"
                ) { pageNumber ->
                    Text(
                        text = " $pageNumber",
                        style = textStyle.copy(fontFeatureSettings = FontFeatureSettings.Mono),
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                    )
                }
                Text(
                    text = "/${onboarding.pageCount}",
                    style = textStyle.copy(fontFeatureSettings = FontFeatureSettings.Mono),
                    color = UiKitTheme.colorsReworked.text.general.regular.disabled,
                )
            }
        }
    }

    @Composable
    fun OnboardingPage(
        onboarding: Onboarding,
        onRequestNotificationsPermissionClicked: () -> Unit,
        onDetectCityClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AnimatedContent(
            targetState = onboarding.currentPage,
            transitionSpec = {
                val isForward = targetState.number > initialState.number
                val slideInSign = if (isForward) 1 else -1
                val animationSpec = tween<IntOffset>(durationMillis = 300, delayMillis = 1)
                val enter = slideInHorizontally(animationSpec) { it * slideInSign }
                val exit = slideOutHorizontally(animationSpec) { -it * slideInSign }
                (enter togetherWith exit).using(SizeTransform(clip = false))
            },
            label = "OnboardingPage",
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
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(text = stringResource(R.string.detect_city).uppercase())
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                ZarinaButton(
                                    onClick = { /*TODO*/ },
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
                            title = stringResource(R.string.onboarding_city_confirmation_title),
                            body = stringResource(R.string.onboarding_city_confirmation_body),
                            buttons = {
                                ZarinaButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(text = stringResource(R.string.yes_correct).uppercase())
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                ZarinaButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ZarinaButtonDefaults.outlineColors(),
                                ) {
                                    Text(text = stringResource(R.string.no_change).uppercase())
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                ZarinaButton(
                                    onClick = { /*TODO*/ },
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
                style = UiKitTheme.typographyReworked.primaryText.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = body,
                style = UiKitTheme.typographyReworked.secondaryText.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
            )

            Spacer(modifier = Modifier.height(20.dp))

            buttons()
        }
    }
}
