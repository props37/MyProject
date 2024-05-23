package ru.livetyping.zarina.presentation.common.component.divider

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaDivider(
    modifier: Modifier = Modifier,
    color: Color = ColorDefault,
    thickness: Dp = 0.5.dp,
) {
    Divider(
        color = color,
        thickness = thickness,
        modifier = modifier,
    )
}

private val ColorDefault: Color
    @Composable
    get() = UiKitTheme.colors.border.general.default
