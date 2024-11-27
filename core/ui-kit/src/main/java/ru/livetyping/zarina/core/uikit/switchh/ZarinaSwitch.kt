package ru.livetyping.zarina.core.uikit.switchh

import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaSwitch(
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

public object ZarinaSwitchDefaults {
    @Composable
    public fun colors(
        checkedThumbColor: Color = UiKitTheme.colors.background.general.regular.default,
        checkedTrackColor: Color = UiKitTheme.colors.background.general.inversed.default,
        uncheckedThumbColor: Color = UiKitTheme.colors.background.general.regular.default,
        uncheckedTrackColor: Color = UiKitTheme.colors.background.skeleton,
    ): SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedTrackAlpha = 1f,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedTrackAlpha = 1f,
    )
}