package ru.livetyping.zarina.core.uikit.counter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaCounter(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaCounterDefaults.BackgroundColor,
    contentColor: Color = ZarinaCounterDefaults.ContentColor,
    contentPadding: PaddingValues = ZarinaCounterDefaults.ContentPaddingSlot,
    content: @Composable BoxScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .drawBehind {
                    drawCircle(color = backgroundColor, radius = this.size.maxDimension / 2)
                }
                .padding(contentPadding),
            content = content,
        )
    }
}

@Composable
public fun ZarinaCounter(
    value: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = ZarinaCounterDefaults.TextStyle,
    textColor: Color = ZarinaCounterDefaults.ContentColor,
    backgroundColor: Color = ZarinaCounterDefaults.BackgroundColor,
    contentPadding: PaddingValues = ZarinaCounterDefaults.ContentPaddingText,
) {
    ZarinaCounter(
        backgroundColor = backgroundColor,
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

public object ZarinaCounterDefaults {
    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.inversed.default

    public val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.inversed.default

    public val TextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.caption2.bold

    public val ContentPaddingSlot: PaddingValues
        get() = PaddingValues(PaddingDefault)

    public val ContentPaddingText: PaddingValues
        get() = PaddingValues(
            start = PaddingDefault,
            top = 1.dp, // Circe font padding
            end = PaddingDefault,
        )

    private val PaddingDefault: Dp get() = 6.dp
}
