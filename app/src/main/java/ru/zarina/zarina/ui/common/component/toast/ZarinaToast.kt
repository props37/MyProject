package ru.zarina.zarina.ui.common.component.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.base.message.ZarinaMessage
import ru.zarina.zarina.ui.base.message.ZarinaMessageStyle
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.base.text.textString
import ru.zarina.zarina.ui.common.tooling.FakeDataGenerator
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaToast(
    message: ZarinaMessage,
    modifier: Modifier = Modifier,
    backgroundColor: Color = message.style.backgroundColor,
    contentColor: Color = message.style.contentColor,
    shape: Shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    windowInsets: WindowInsets = WindowInsets.safeDrawing
        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = modifier
                .background(backgroundColor)
                .clip(shape)
                .windowInsetsPadding(windowInsets)
                .padding(contentPadding),
        ) {
            Text(
                text = textString(message.text),
                style = UiKitTheme.typography.tertiary.light,
            )
        }
    }
}

private val ZarinaMessageStyle.backgroundColor: Color
    @Composable
    get() = when (this) {
        ZarinaMessageStyle.DEFAULT -> UiKitTheme.colors.background.general.inversed.default
        ZarinaMessageStyle.ERROR -> UiKitTheme.colors.background.accent.pink
    }

private val ZarinaMessageStyle.contentColor: Color
    @Composable
    get() = when (this) {
        ZarinaMessageStyle.DEFAULT -> UiKitTheme.colors.text.general.inversed.default
        ZarinaMessageStyle.ERROR -> UiKitTheme.colors.text.general.accent.red
    }

@Preview
@Composable
private fun PreviewDefault() {
    ZarinaPreview {
        val message = remember {
            ZarinaMessage(
                text = Text.String(FakeDataGenerator.getLoremIpsum(10))
            )
        }

        ZarinaToast(
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
            ZarinaMessage(
                text = Text.String(FakeDataGenerator.getLoremIpsum(10)),
                style = ZarinaMessageStyle.ERROR,
            )
        }

        ZarinaToast(
            message = message,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
