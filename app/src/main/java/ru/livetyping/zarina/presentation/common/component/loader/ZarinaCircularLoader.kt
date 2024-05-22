package ru.livetyping.zarina.presentation.common.component.loader

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaCircularLoader(
    modifier: Modifier = Modifier,
    color: Color = ColorDefault,
    strokeWidth: Dp = 2.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    CircularProgressIndicator(
        color = color,
        strokeWidth = strokeWidth,
        strokeCap = strokeCap,
        modifier = modifier,
    )
}

private val ColorDefault: Color
    @Composable
    get() = UiKitTheme.colors.icon.regular.default
