package ru.livetyping.zarina.core.uikit.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = ZarinaItemDefaults.BackgroundColor,
    contentColor: Color = ZarinaItemDefaults.ContentColor,
    contentPadding: PaddingValues = ZarinaItemDefaults.ContentPadding,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    startContent: @Composable RowScope.() -> Unit,
) {
    ZarinaItem(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        contentPadding = contentPadding,
        startContent = startContent,
        endContent = endContent,
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            ),
    )
}

@Composable
public fun ZarinaItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaItemDefaults.BackgroundColor,
    contentColor: Color = ZarinaItemDefaults.ContentColor,
    contentPadding: PaddingValues = ZarinaItemDefaults.ContentPadding,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    startContent: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides UiKitTheme.typography.primary.regular,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .defaultMinSize(minHeight = ZarinaItemDefaults.MinHeight)
                .drawBehind { drawRect(backgroundColor) }
                .padding(contentPadding),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = startContent,
                modifier = Modifier.weight(1f),
            )
            endContent?.let { content ->
                Spacer(modifier = Modifier.width(16.dp))
                content()
            }
        }
    }
}

public object ZarinaItemDefaults {
    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    public val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    public val ContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    public val MinHeight: Dp get() = 56.dp
}
