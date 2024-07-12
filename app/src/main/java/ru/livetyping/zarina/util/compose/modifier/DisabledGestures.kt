package ru.livetyping.zarina.util.compose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.disabledGestures(disabled: Boolean = true): Modifier {
    return if (disabled) {
        this.pointerInput(Unit) {
            awaitPointerEventScope {
                awaitPointerEvent(PointerEventPass.Initial)
                    .changes
                    .forEach { it.consume() }
            }
        }
    } else {
        this
    }
}
