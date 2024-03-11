package ru.zarina.zarina.ui.common.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 8.dp),
    ) {
        Text(
            text = text,
            style = UiKitTheme.typography.circle1720bold,
            color = UiKitTheme.colorsOld.primaryContentColor,
        )
    }
}
