package ru.livetyping.zarina.core.uikit.switchh

import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

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
        checkedThumbColor: Color = UiKitTheme2.colors.white,
        checkedTrackColor: Color = UiKitTheme2.colors.mainBlack,
        uncheckedThumbColor: Color = UiKitTheme2.colors.white,
        uncheckedTrackColor: Color = UiKitTheme2.colors.gray,
    ): SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        checkedTrackAlpha = 1f,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedTrackAlpha = 1f,
    )
}