package ru.zarina.zarina.ui.common.component.snack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.base.text.textString
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.rippletheme.DarkRippleTheme
import ru.zarina.zarina.ui.common.rippletheme.LightRippleTheme
import ru.zarina.zarina.ui.common.zarinasnack.ZarinaSnackMessage
import ru.zarina.zarina.ui.common.zarinasnack.ZarinaSnackMessageStyle
import ru.zarina.zarina.ui.theme.UiKitTheme

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
            )

            Spacer(modifier = Modifier.weight(1f))

            if (message.button != null) {
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
