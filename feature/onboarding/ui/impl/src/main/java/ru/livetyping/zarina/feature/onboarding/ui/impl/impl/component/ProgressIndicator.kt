package ru.livetyping.zarina.feature.onboarding.ui.impl.impl.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uicompose.FontFeatureSettings
import ru.livetyping.zarina.core.uikit.progress.ZarinaLinearProgressIndicator
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingState

@Composable
internal fun ProgressIndicator(
    onboardingState: OnboardingState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val currentOnboardingStepNumber = onboardingState.currentStepIndex + 1
        val onboardingProgress =
            currentOnboardingStepNumber / onboardingState.onboardingSteps.size.toFloat()

        ZarinaLinearProgressIndicator(
            progress = onboardingProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            val textStyle = UiKitTheme.typography.tertiary.regular

            Text(
                text = stringResource(R.string.res_step),
                style = textStyle,
                color = UiKitTheme.colors.text.general.regular.default,
            )
            AnimatedContent(
                targetState = currentOnboardingStepNumber,
                transitionSpec = {
                    // Default AnimatedContent transitionSpec without scaling
                    fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90))
                        .togetherWith(fadeOut(animationSpec = tween(durationMillis = 90)))
                },
                label = "ProgressIndicator current step number",
            ) { stepNumber ->
                Text(
                    text = " $stepNumber",
                    style = textStyle.copy(fontFeatureSettings = FontFeatureSettings.Mono),
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            }
            Text(
                text = "/${onboardingState.onboardingSteps.size}",
                style = textStyle.copy(fontFeatureSettings = FontFeatureSettings.Mono),
                color = UiKitTheme.colors.text.general.regular.disabled,
            )
        }
    }
}
