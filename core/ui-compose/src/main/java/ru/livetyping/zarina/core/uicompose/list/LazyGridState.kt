package ru.livetyping.zarina.core.uicompose.list

import androidx.compose.foundation.lazy.grid.LazyGridState

public val LazyGridState.canScroll: Boolean
    get() = canScrollForward || canScrollBackward
