package ru.zarina.zarina.ui.common.components.filters

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun FiltersDivider(
    modifier: Modifier = Modifier,
) {
    Divider(
        thickness = 1.dp,
        color = UiKitTheme.colors.listDivider,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}
