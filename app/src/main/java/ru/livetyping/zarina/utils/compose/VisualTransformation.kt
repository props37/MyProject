package ru.livetyping.zarina.utils.compose

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

abstract class SeparatorVisualTransformation : VisualTransformation {

    abstract fun transform(input: CharSequence): CharSequence

    abstract fun isSeparator(char: Char): Boolean

    override fun filter(text: AnnotatedString): TransformedText {

        val formatted = transform(text)

        return TransformedText(
            text = AnnotatedString(text = formatted.toString()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val transformedOffsets = formatted
                        .mapIndexedNotNull { index, c ->
                            index
                                .takeIf { !isSeparator(c) }
                                ?.plus(1)
                        }
                        .let { offsetList ->
                            listOf(0) + offsetList
                        }

                    return transformedOffsets[offset]
                }

                override fun transformedToOriginal(offset: Int): Int =
                    formatted
                        .mapIndexedNotNull { index, c ->
                            index.takeIf { isSeparator(c) }
                        }
                        .count { separatorIndex ->
                            separatorIndex < offset
                        }
                        .let { separatorCount ->
                            offset - separatorCount
                        }
            }
        )
    }
}

object PhoneVisualTransformation : SeparatorVisualTransformation() {
    override fun transform(input: CharSequence): CharSequence {
        return buildString {
            input.forEachIndexed { index, c ->
                when (index) {
                    0, 1 -> append(c)
                    2 -> {
                        append(" (")
                        append(c)
                    }

                    3, 4 -> append(c)
                    5 -> {
                        append(") ")
                        append(c)
                    }

                    6, 7 -> append(c)

                    8 -> {
                        append("-")
                        append(c)
                    }

                    9 -> append(c)

                    10 -> {
                        append("-")
                        append(c)
                    }

                    11 -> append(c)

                    12 -> {
                        append("-")
                        append(c)
                    }

                    13 -> append(c)

                }
            }
        }
    }

    override fun isSeparator(char: Char): Boolean {
        return !char.isDigit() && char != '+'
    }
}
