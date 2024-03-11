package ru.zarina.zarina.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

// TODO: [Low] Replace with fully custom Switcher

@Composable
fun ZarinaSwitch(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: SwitchColors = ZarinaSwitchDefaults.colors(),
) {
    Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChanged,
        colors = colors,
        modifier = modifier,
    )
}

object ZarinaSwitchDefaults {
    @Composable
    fun colors(
        checkedThumbColor: Color = UiKitTheme.colorsReworked.background.general.regular.default,
        checkedTrackColor: Color = UiKitTheme.colorsReworked.background.general.inversed.default,
        uncheckedThumbColor: Color = UiKitTheme.colorsReworked.background.general.regular.default,
        uncheckedTrackColor: Color = UiKitTheme.colorsReworked.background.skeleton,
    ): SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedBorderColor = Color.Transparent,
        checkedIconColor = Color.Transparent,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedBorderColor = Color.Transparent,
        uncheckedIconColor = Color.Transparent,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var isChecked by remember { mutableStateOf(false) }
        ZarinaSwitch(
            isChecked = isChecked,
            onCheckedChanged = { isChecked = it },
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
