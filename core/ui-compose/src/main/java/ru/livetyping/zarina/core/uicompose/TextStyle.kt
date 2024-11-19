package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density

@Stable
public fun TextStyle.unscalable(density: Density): TextStyle {
    val unscalableFontSize = this.fontSize.unscalable(density)
    return this.copy(fontSize = unscalableFontSize)
}
