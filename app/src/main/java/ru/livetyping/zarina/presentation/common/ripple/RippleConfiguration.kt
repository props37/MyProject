package ru.livetyping.zarina.presentation.common.ripple

import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.RippleConfiguration
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class)
@Stable
val LightRippleConfiguration: RippleConfiguration
    get() = RippleConfiguration(Color.White)

@OptIn(ExperimentalMaterialApi::class)
@Stable
val DarkRippleConfiguration: RippleConfiguration
    get() = RippleConfiguration(Color.Black)
