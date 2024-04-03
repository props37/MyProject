package ru.livetyping.zarina.utils.compose

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun topLineShape(height: Dp) = with(LocalDensity.current) {
    GenericShape { size, _ ->
        val heightPx = height.toPx()
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, heightPx)
        lineTo(0f, heightPx)
        close()
    }
}
