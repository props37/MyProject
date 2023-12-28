package ru.zarina.zarina.ui.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme

// TODO: [Low] Add maxWidth to support landscape orientation

@Composable
fun ZarinaDialogContainer(
    modifier: Modifier = Modifier,
    color: Color = UiKitTheme.colorsReworked.background.general.regular.background,
    shape: Shape = RoundedCornerShape(4.dp),
    elevation: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .shadow(elevation = elevation, shape = shape)
            .background(color = color, shape = shape)
            .padding(contentPadding),
        content = content,
    )
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
                    style = UiKitTheme.typographyReworked.primary.bold,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Текущая геолокация: Санкт-Петербург. Ты можешь изменить геолокацию в любой момент в настройках Профиля.",
                    style = UiKitTheme.typographyReworked.secondary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
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
