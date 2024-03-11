package ru.zarina.zarina.ui.screens.catalog.filters.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.components.material.zarinaColors
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun SwitchItem(
    filterName: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(onClick = { onCheckedChange(!isChecked) })
    ) {
        Text(
            text = filterName,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.zarinaColors(),
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}
