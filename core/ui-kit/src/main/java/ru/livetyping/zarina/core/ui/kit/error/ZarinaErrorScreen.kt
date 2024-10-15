package ru.livetyping.zarina.core.ui.kit.error

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.ui.compose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.ui.kit.button.ZarinaButton
import ru.livetyping.zarina.core.ui.kit.text.textString
import ru.livetyping.zarina.core.ui.kit.theme.UiKitTheme

@Composable
public fun ZarinaErrorScreen(
    state: ZarinaErrorScreenState,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    fillWholeHeight: Boolean = true,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val weightModifier = Modifier.weight(weight = 1f, fill = fillWholeHeight)

        Spacer(modifier = weightModifier)
        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            painter = painterResource(state.iconResId),
            contentDescription = null,
            tint = UiKitTheme.colors.icon.regular.disabled,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = textString(state.title),
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
            targetState = state.buttonState.isButtonVisible,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
            },
            label = "ZarinaErrorScreen button",
        ) { isVisible ->
            if (isVisible) {
                ZarinaButton(
                    onClick = onButtonClicked,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = textString(state.buttonState.buttonText).uppercase())
                }
            }
        }
    }
}
