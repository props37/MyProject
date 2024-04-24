package ru.livetyping.zarina.ui.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.ui.theme.ZarinaTheme

// TODO: [Low] Add maxWidth to support landscape orientation

@Composable
fun ZarinaDialogContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundColor,
    contentColor: Color = ContentColor,
    shape: Shape = Shape,
    elevation: Dp = Elevation,
    contentPadding: PaddingValues = ContentPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            horizontalAlignment = horizontalAlignment,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .shadow(elevation = elevation, shape = shape)
                .background(color = backgroundColor, shape = shape)
                .padding(contentPadding),
            content = content,
        )
    }
}

@Composable
fun ZarinaDialogContainer(
    title: @Composable () -> Unit,
    body: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BackgroundColor,
    contentColor: Color = ContentColor,
    shape: Shape = Shape,
    elevation: Dp = Elevation,
    contentPadding: PaddingValues = ContentPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    ZarinaDialogContainer(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides UiKitTheme.typography.primary.bold,
        ) {
            title()
        }

        Spacer(modifier = Modifier.height(10.dp))

        CompositionLocalProvider(
            LocalTextStyle provides UiKitTheme.typography.secondary.regular,
        ) {
            body()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = buttons,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaTheme {
        ZarinaDialogContainer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column {
                Text(
                    text = "Мы определили твой город",
                    style = UiKitTheme.typography.primary.bold,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Текущая геолокация: Санкт-Петербург. Ты можешь изменить геолокацию в любой момент в настройках Профиля.",
                    style = UiKitTheme.typography.secondary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
                Spacer(modifier = Modifier.height(20.dp))
                ZarinaButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "ЗАКРЫТЬ")
                }
            }
        }
    }
}

private val BackgroundColor: Color
    @Composable
    get() = UiKitTheme.colors.background.general.regular.default

private val ContentColor: Color
    @Composable
    get() = UiKitTheme.colors.text.general.regular.default

private val Shape: Shape get() = RoundedCornerShape(4.dp)
private val Elevation: Dp get() = 12.dp

private val ContentPadding: PaddingValues
    get() = PaddingValues(24.dp)
