package ru.zarina.zarina.util.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides

val WindowInsets.Companion.none: WindowInsets
    get() = WindowInsets(0, 0, 0, 0)

val WindowInsetsSides.Companion.HorizontalAndBottom: WindowInsetsSides
    get() = Horizontal + Bottom
