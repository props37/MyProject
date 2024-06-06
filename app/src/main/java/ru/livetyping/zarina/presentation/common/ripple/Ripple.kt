package ru.livetyping.zarina.presentation.common.ripple

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.material.ripple
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
val LightRipple: IndicationNodeFactory
    get() = ripple(color = Color.White)

@Stable
val DarkRipple: IndicationNodeFactory
    get() = ripple(color = Color.Black)
