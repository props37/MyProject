package ru.livetyping.zarina.util.compose.text

import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.placeCursorAtEnd

fun TextFieldBuffer.clear() {
    delete(0, this.length)
    placeCursorAtEnd()
}
