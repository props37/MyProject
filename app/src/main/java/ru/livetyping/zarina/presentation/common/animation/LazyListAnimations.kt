package ru.livetyping.zarina.presentation.common.animation

import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.IntOffset

val LazyListFadeInSpec = spring<Float>()
val LazyListPlacementSpec = spring<IntOffset>()
val LazyListFadeOutSpec = LazyListFadeInSpec
