package ru.livetyping.zarina.presentation.common.component.snack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.rippletheme.DarkRippleTheme
import ru.livetyping.zarina.presentation.common.rippletheme.LightRippleTheme
import ru.livetyping.zarina.presentation.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessage
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessageButton
import ru.livetyping.zarina.presentation.common.zarinasnack.ZarinaSnackMessageStyle
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaSnack(
    message: ZarinaSnackMessage,
    modifier: Modifier = Modifier,
    backgroundColor: Color = message.style.backgroundColor,
    contentColor: Color = message.style.contentColor,
    shape: Shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
) {
    val rippleTheme = when (message.style) {
        ZarinaSnackMessageStyle.DEFAULT -> LightRippleTheme
        ZarinaSnackMessageStyle.ERROR -> DarkRippleTheme
    }

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalRippleTheme provides rippleTheme,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .defaultMinSize(minHeight = 56.dp)
                .background(backgroundColor)
                .clip(shape)
                .padding(contentPadding),
        ) {
            Text(
                text = textString(message.text),
                style = UiKitTheme.typography.tertiary.light,
                modifier = Modifier.weight(1f),
            )

            if (message.button != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = message.button.onClick) {
                    Text(
                        text = textString(message.button.text).uppercase(),
                        style = UiKitTheme.typography.caption1.regular,
                    )
                }
            }
        }
    }
}

@Composable
private fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = ZarinaButtonDefaults.Shape,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        content = content,
        modifier = modifier
            .clip(shape)
            .defaultMinSize(minHeight = 40.dp)
            .clickable(onClick = onClick)
            .padding(contentPadding),
    )
}

@Preview
@Composable
private fun PreviewDefaultShort() {
    ZarinaPreview {
        val message = remember {
            ZarinaSnackMessage(
                text = Text.String(FakeDataGenerator.getLoremIpsum(1)),
                button = ZarinaSnackMessageButton(
                    text = Text.String("Button"),
                    onClick = {},
                )
            )
        }

        ZarinaSnack(
            message = message,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultLong() {
    ZarinaPreview {
        val message = remember {
            ZarinaSnackMessage(
                text = Text.String(FakeDataGenerator.getLoremIpsum(10)),
                button = ZarinaSnackMessageButton(
                    text = Text.String("Button"),
                    onClick = {},
                )
            )
        }

        ZarinaSnack(
            message = message,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultLongNoButton() {
    ZarinaPreview {
        val message = remember {
            ZarinaSnackMessage(
                text = Text.String(FakeDataGenerator.getLoremIpsum(10)),
            )
        }

        ZarinaSnack(
            message = message,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewError() {
    ZarinaPreview {
        val message = remember {
            ZarinaSnackMessage(
                text = Text.String(FakeDataGenerator.getLoremIpsum(10)),
                style = ZarinaSnackMessageStyle.ERROR,
            )
        }

        ZarinaSnack(
            message = message,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val ZarinaSnackMessageStyle.backgroundColor: Color
    @Composable
    get() = when (this) {
        ZarinaSnackMessageStyle.DEFAULT -> UiKitTheme.colors.background.general.inversed.default
        ZarinaSnackMessageStyle.ERROR -> UiKitTheme.colors.background.accent.pink
    }

private val ZarinaSnackMessageStyle.contentColor: Color
    @Composable
    get() = when (this) {
        ZarinaSnackMessageStyle.DEFAULT -> UiKitTheme.colors.text.general.inversed.default
        ZarinaSnackMessageStyle.ERROR -> UiKitTheme.colors.text.general.accent.red
    }
