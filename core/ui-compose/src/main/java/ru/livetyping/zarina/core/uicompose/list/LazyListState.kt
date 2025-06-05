package ru.livetyping.zarina.core.uicompose.list

import androidx.compose.foundation.lazy.LazyListState

public val LazyListState.canScroll: Boolean
    get() = canScrollForward || canScrollBackward
