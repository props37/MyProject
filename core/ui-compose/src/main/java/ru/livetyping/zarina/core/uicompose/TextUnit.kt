package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit

@Stable
public fun TextUnit.unscalable(density: Density): TextUnit {
    val textUnit = this
    return with(density) { textUnit / fontScale }
}
