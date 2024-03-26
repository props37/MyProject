package ru.zarina.zarina.util.compose.text

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density

@Stable
fun TextStyle.unscalable(density: Density): TextStyle {
    val unscalableFontSize = this.fontSize.unscalable(density)
    return this.copy(fontSize = unscalableFontSize)
}
