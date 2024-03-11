package ru.zarina.zarina.ui.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun ScreenPlaceholder(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = title,
            style = UiKitTheme.typography.circle2028,
            color = UiKitTheme.colors.primaryContentColor,
            modifier = Modifier.padding(32.dp)
        )
    }
}
