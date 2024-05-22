package ru.livetyping.zarina.presentation.common.component.switchh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Switch
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwitchColors
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

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
