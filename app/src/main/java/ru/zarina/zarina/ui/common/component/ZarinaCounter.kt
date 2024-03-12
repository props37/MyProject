package ru.zarina.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaCounter(
    value: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = UiKitTheme.typography.caption2.bold,
    textColor: Color = UiKitTheme.colors.text.general.inversed.default,
    backgroundColor: Color = UiKitTheme.colors.background.general.inversed.default,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = ContentPadding,
) {
    Text(
        text = value,
        style = textStyle,
        color = textColor,
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .padding(contentPadding),
    )
}

private val ContentPadding: PaddingValues get() = PaddingValues(
    start = ContentPaddingHorizontal,
    top = 1.dp, // Circe font padding
    end = ContentPaddingHorizontal,
)
private val ContentPaddingHorizontal: Dp get() = 8.dp
