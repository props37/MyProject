package ru.livetyping.zarina.core.uikit.tab

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaTab(
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    selectedTextStyle: TextStyle = ZarinaTabDefaults.SelectedTextStyle,
    unselectedTextStyle: TextStyle = ZarinaTabDefaults.UnselectedTextStyle,
    selectedContentColor: Color = ZarinaTabDefaults.SelectedContentColor,
    unselectedContentColor: Color = ZarinaTabDefaults.UnselectedContentColor,
    content: @Composable RowScope.() -> Unit,
) {
    val textStyle = if (isSelected) selectedTextStyle else unselectedTextStyle
    val contentColor = if (isSelected) selectedContentColor else unselectedContentColor

    ZarinaButton(
        onClick = onClick,
        size = ZarinaButtonSize.Medium,
        colors = ZarinaButtonDefaults.backlessColors(contentColor = contentColor),
        contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        textStyle = textStyle,
        modifier = modifier,
        content = content,
    )
}

@Composable
public fun ZarinaTab(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    selectedTextStyle: TextStyle = ZarinaTabDefaults.SelectedTextStyle,
    unselectedTextStyle: TextStyle = ZarinaTabDefaults.UnselectedTextStyle,
    selectedContentColor: Color = ZarinaTabDefaults.SelectedContentColor,
    unselectedContentColor: Color = ZarinaTabDefaults.UnselectedContentColor,
) {
    ZarinaTab(
        onClick = onClick,
        isSelected = isSelected,
        selectedTextStyle = selectedTextStyle,
        unselectedTextStyle = unselectedTextStyle,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        modifier = modifier,
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

public object ZarinaTabDefaults {
    public val SelectedTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.regular

    public val UnselectedTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    public val SelectedContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.button.backless.default

    public val UnselectedContentColor: Color
        @Composable
        get() = UiKitTheme.colors.text.button.backless.default
}
