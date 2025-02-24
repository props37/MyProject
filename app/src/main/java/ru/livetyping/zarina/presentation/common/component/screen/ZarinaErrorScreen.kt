package ru.livetyping.zarina.presentation.common.component.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.presentation.theme.ZarinaTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

@Composable
fun ZarinaErrorScreen(
    state: ErrorState,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    fillWholeHeight: Boolean = true,
) {
    val title = textString(state.title)

    DisposableEffect(title) {
        AppMetricaHelper.reportErrorShown(title)
        onDispose {}
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val weightModifier = Modifier.weight(weight = 1f, fill = fillWholeHeight)
        Spacer(modifier = weightModifier)

        Icon(
            painter = painterResource(state.iconResId),
            contentDescription = null,
            tint = UiKitTheme.colors.icon.regular.disabled,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = UiKitTheme.typography.primary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = textString(state.body),
            style = UiKitTheme.typography.secondary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        )

        Spacer(modifier = weightModifier)
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = state.isButtonVisible,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
            },
            label = "ZarinaErrorScreen Refresh button",
        ) { isVisible ->
            if (isVisible) {
                ZarinaButton(
                    onClick = onButtonClicked,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = textString(state.buttonText).uppercase())
                }
            }
        }
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun NetworkErrorPreview() {
    ZarinaTheme {
        ZarinaErrorScreen(
            state = ErrorState.NETWORK,
            onButtonClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun GenericErrorPreview() {
    ZarinaTheme {
        ZarinaErrorScreen(
            state = ErrorState.GENERIC,
            onButtonClicked = {},
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
