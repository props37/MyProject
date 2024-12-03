package ru.livetyping.zarina.core.uikit.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

// TODO: [Low] Add maxWidth to support landscape orientation

@Composable
public fun ZarinaDialogContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaDialogContainerDefaults.BackgroundColor,
    contentColor: Color = ZarinaDialogContainerDefaults.ContentColor,
    shape: Shape = ZarinaDialogContainerDefaults.Shape,
    elevation: Dp = ZarinaDialogContainerDefaults.Elevation,
    contentPadding: PaddingValues = ZarinaDialogContainerDefaults.ContentPadding,
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
public fun ZarinaDialogContainer(
    title: @Composable () -> Unit,
    body: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ZarinaDialogContainerDefaults.BackgroundColor,
    contentColor: Color = ZarinaDialogContainerDefaults.ContentColor,
    shape: Shape = ZarinaDialogContainerDefaults.Shape,
    elevation: Dp = ZarinaDialogContainerDefaults.Elevation,
    contentPadding: PaddingValues = ZarinaDialogContainerDefaults.ContentPadding,
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

public object ZarinaDialogContainerDefaults {
    public val BackgroundColor: Color
        @Composable
        get() = UiKitTheme.colors.background.general.regular.default

    public val ContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default

    public val Shape: Shape get() = RoundedCornerShape(4.dp)
    public val Elevation: Dp get() = 12.dp
    public val ContentPadding: PaddingValues get() = PaddingValues(24.dp)
}
