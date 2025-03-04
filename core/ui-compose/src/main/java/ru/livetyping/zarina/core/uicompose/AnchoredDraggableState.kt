package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.gestures.AnchoredDraggableState

public fun <T> AnchoredDraggableState<T>.requireCoercedOffset(): Float {
    val minOffset = this.anchors.minPosition()
    val maxOffset = this.anchors.maxPosition()
    return this.requireOffset().coerceIn(minOffset, maxOffset)
}

/**
 * Strongly consider using [requireCoercedOffset] which will throw if the offset is read before
 * it is initialized.
 */
public val <T> AnchoredDraggableState<T>.coercedOffset: Float
    get() {
        val offset = this.offset
        if (offset.isNaN()) return offset
        val minOffset = this.anchors.minPosition()
        val maxOffset = this.anchors.maxPosition()
        return offset.coerceIn(minOffset, maxOffset)
    }
