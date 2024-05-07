package ru.livetyping.zarina.presentation.common.component.tab

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaTab(
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    selectedTextStyle: TextStyle = UiKitTheme.typography.secondary.regular,
    unselectedTextStyle: TextStyle = UiKitTheme.typography.secondary.light,
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
fun ZarinaTab(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    selectedTextStyle: TextStyle = UiKitTheme.typography.secondary.regular,
    unselectedTextStyle: TextStyle = UiKitTheme.typography.secondary.light,
) {
    ZarinaTab(
        onClick = onClick,
        isSelected = isSelected,
        selectedTextStyle = selectedTextStyle,
        unselectedTextStyle = unselectedTextStyle,
        modifier = modifier,
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun PreviewSlot() {
    ZarinaPreview {
        ZarinaTabRow(selectedTabIndex = 0) {
            ZarinaTab(onClick = {}, isSelected = true) {
                Text(text = "Женщинам".uppercase())
            }
            ZarinaTab(onClick = {}, isSelected = false) {
                Text(text = "Мужчинам".uppercase())
            }
        }
    }
}

@Preview
@Composable
private fun PreviewText() {
    ZarinaPreview {
        ZarinaTabRow(selectedTabIndex = 0) {
            ZarinaTab(
                text = "Женщинам".uppercase(),
                onClick = {},
                isSelected = true,
            )
            ZarinaTab(
                text = "Мужчинам".uppercase(),
                onClick = {},
                isSelected = false,
            )
        }
    }
}
