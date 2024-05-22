package ru.livetyping.zarina.presentation.theme

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.darkColors
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun ZarinaTheme(
    inDarkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val materialColors = remember(inDarkTheme) {
        if (inDarkTheme) {
            darkColors(primary = Colors.MineShaftDark)
        } else {
            lightColors(primary = Colors.MineShaftDark)
        }
    }

    CompositionLocalProvider(
        LocalUiKitColors provides LightUiKitColors,
        LocalUiKitTypography provides UiKitTypography(),
    ) {
        MaterialTheme(
            colors = materialColors,
            content = content,
        )
    }
}
