package ru.livetyping.zarina.util.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable

val WindowInsets.Companion.none: WindowInsets
    get() = WindowInsets(0, 0, 0, 0)

val WindowInsets.Companion.navigationBarsOrIme: WindowInsets
    @Composable
    get() = navigationBars.union(ime)

val WindowInsetsSides.Companion.HorizontalAndBottom: WindowInsetsSides
    get() = Horizontal + Bottom
