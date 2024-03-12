package ru.zarina.zarina.ui.common.component.counter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaCounter(
    modifier: Modifier = Modifier,
    backgroundColor: Color = UiKitTheme.colors.background.general.inversed.default,
    contentColor: Color = UiKitTheme.colors.text.general.inversed.default,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = ContentPaddingSlot,
    content: @Composable BoxScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(backgroundColor, shape)
                .padding(contentPadding),
            content = content,
        )
    }
}

@Composable
fun ZarinaCounter(
    value: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = UiKitTheme.typography.caption2.bold,
    textColor: Color = UiKitTheme.colors.text.general.inversed.default,
    backgroundColor: Color = UiKitTheme.colors.background.general.inversed.default,
    shape: Shape = CircleShape,
    contentPadding: PaddingValues = ContentPaddingText,
) {
    ZarinaCounter(
        backgroundColor = backgroundColor,
        shape = shape,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        Text(
            text = value,
            style = textStyle,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun PreviewSlot() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ZarinaCounter {
                Text(text = "Slot")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewText() {
    ZarinaPreview {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            ZarinaCounter(value = "1")
        }
    }
}

private val ContentPaddingSlot: PaddingValues get() = PaddingValues(PaddingDefault)
private val ContentPaddingText: PaddingValues get() = PaddingValues(
    start = PaddingDefault,
    top = 1.dp, // Circe font padding
    end = PaddingDefault,
)
private val PaddingDefault: Dp get() = 6.dp
