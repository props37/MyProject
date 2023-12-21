package ru.zarina.zarina.util.compose

import androidx.compose.foundation.layout.WindowInsetsSides

val WindowInsetsSides.Companion.HorizontalAndBottom: WindowInsetsSides
    get() = Start + End + Bottom
