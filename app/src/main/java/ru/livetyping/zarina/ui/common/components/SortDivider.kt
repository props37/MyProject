package ru.livetyping.zarina.ui.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun SortDivider() {
    Divider(
        thickness = 1.dp,
        color = UiKitTheme.colorsOld.listDivider,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}
