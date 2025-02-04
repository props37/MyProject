package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
public fun <T> rememberAnchoredDraggableState(
    initialValue: T,
): AnchoredDraggableState<T> {
    return remember(initialValue) {
        AnchoredDraggableState(initialValue)
    }
}

@Composable
public fun <T> rememberAnchoredDraggableState(
    initialValue: T,
    anchors: DraggableAnchors<T>,
): AnchoredDraggableState<T> {
    return remember(initialValue, anchors) {
        AnchoredDraggableState(
            initialValue = initialValue,
            anchors = anchors,
        )
    }
}

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
