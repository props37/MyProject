package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable

public val WindowInsets.Companion.none: WindowInsets
    get() = WindowInsets(0, 0, 0, 0)

public val WindowInsets.Companion.navigationBarsWithIme: WindowInsets
    @Composable
    get() = navigationBars.union(ime)
