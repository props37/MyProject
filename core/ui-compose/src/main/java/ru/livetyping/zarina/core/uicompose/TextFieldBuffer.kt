package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.placeCursorAtEnd

public fun TextFieldBuffer.clear() {
    delete(0, this.length)
    placeCursorAtEnd()
}

public fun TextFieldBuffer.setTextAndPlaceCursorAtEnd(text: CharSequence) {
    replace(0, length, text)
    placeCursorAtEnd()
}
