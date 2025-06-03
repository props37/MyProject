package ru.livetyping.zarina.feature.wishlist.ui.impl.impl.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density

internal fun Modifier.topWindowInsetsScrimGradient(
    windowInsets: WindowInsets,
    startColor: Color,
    endColor: Color,
): Modifier = this.drawWithCache {
    val density = Density(density)
    val height = windowInsets.getTop(density).toFloat()
    val gradient = Brush.verticalGradient(
        0f to startColor,
        1f to endColor,
        endY = height,
    )

    onDrawWithContent {
        drawContent()
        drawRect(
            brush = gradient,
            size = Size(size.width, height),
        )
    }
}
