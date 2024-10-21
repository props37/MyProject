package ru.livetyping.zarina.core.uikit.tab

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    content: @Composable RowScope.() -> Unit,
) {
    val textStyle = if (isSelected) selectedTextStyle else unselectedTextStyle

    ZarinaButton(
        onClick = onClick,
        size = ZarinaButtonSize.Medium,
        colors = ZarinaButtonDefaults.backlessColors(),
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
) {
    ZarinaTab(
        onClick = onClick,
        isSelected = isSelected,
        selectedTextStyle = selectedTextStyle,
        unselectedTextStyle = unselectedTextStyle,
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
    internal val SelectedTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.regular

    internal val UnselectedTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light
}
