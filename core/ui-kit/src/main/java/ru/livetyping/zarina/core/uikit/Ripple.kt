package ru.livetyping.zarina.core.uikit

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.material.ripple
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
public val LightRipple: IndicationNodeFactory
    get() = ripple(color = Color.White)

@Stable
public val DarkRipple: IndicationNodeFactory
    get() = ripple(color = Color.Black)
