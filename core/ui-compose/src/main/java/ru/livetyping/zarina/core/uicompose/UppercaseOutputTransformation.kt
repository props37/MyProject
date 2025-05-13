package ru.livetyping.zarina.core.uicompose

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.runtime.Immutable

@Immutable
public object UppercaseOutputTransformation : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        if (length > 0) {
            replace(
                start = 0,
                end = length,
                text = originalText.toString().uppercase(),
            )
        }
    }
}
