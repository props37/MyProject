package ru.livetyping.zarina.core.uikit.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaToast(
    message: ZarinaToastMessage,
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
            contentAlignment = Alignment.CenterStart,
            modifier = modifier
                .clip(shape)
                .background(backgroundColor)
                .windowInsetsPadding(windowInsets)
                .defaultMinSize(minHeight = 56.dp)
                .padding(contentPadding),
        ) {
            Text(
                text = textString(message.text),
                style = UiKitTheme.typography.tertiary.light,
            )
        }
    }
}

private val ZarinaToastMessageStyle.backgroundColor: Color
    @Composable
    get() = when (this) {
        ZarinaToastMessageStyle.DEFAULT -> UiKitTheme.colors.background.general.inversed.default
        ZarinaToastMessageStyle.ERROR -> UiKitTheme.colors.background.accent.pink
    }

private val ZarinaToastMessageStyle.contentColor: Color
    @Composable
    get() = when (this) {
        ZarinaToastMessageStyle.DEFAULT -> UiKitTheme.colors.text.general.inversed.default
        ZarinaToastMessageStyle.ERROR -> UiKitTheme.colors.text.general.accent.red
    }
