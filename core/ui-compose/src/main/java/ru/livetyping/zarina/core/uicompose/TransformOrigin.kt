package ru.livetyping.zarina.core.uicompose

import androidx.compose.ui.graphics.TransformOrigin

public val TransformOrigin.Companion.BottomEnd: TransformOrigin
    get() = TransformOrigin(1f, 1f)

public val TransformOrigin.Companion.TopEnd: TransformOrigin
    get() = TransformOrigin(1f, 0f)
