package ru.livetyping.zarina.util.compose.text

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.text.Selection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import ru.livetyping.zarina.presentation.common.phone.PhoneNumberUtilProvider
import ru.livetyping.zarina.util.platform.locale

// Source: https://medium.com/google-developer-experts/hands-on-jetpack-compose-visualtransformation-to-create-a-phone-number-formatter-99b0347fc4f6

class PhoneNumberVisualTransformation(
    context: Context,
    countryCode: String = context.locale.country,
) : VisualTransformation {
    private val phoneNumberFormatter =
        PhoneNumberUtilProvider.provide(context).getAsYouTypeFormatter(countryCode)

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text, Selection.getSelectionEnd(text))
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val index = offset.coerceIn(transformation.originalToTransformed.indices)
                return transformation.originalToTransformed[index]
            }
            override fun transformedToOriginal(offset: Int): Int {
                val index = offset.coerceIn(transformation.transformedToOriginal.indices)
                return transformation.transformedToOriginal[index]
            }
        }

        return TransformedText(
            text = AnnotatedString(transformation.formatted.orEmpty()),
            offsetMapping = offsetMapping,
        )
    }

    private fun reformat(s: CharSequence, cursor: Int): Transformation {
        phoneNumberFormatter.clear()

        val curIndex = cursor - 1
        var formatted: String? = null
        var lastNonSeparator = 0.toChar()
        var hasCursor = false

        s.forEachIndexed { index, char ->
            if (PhoneNumberUtils.isNonSeparator(char)) {
                if (lastNonSeparator.code != 0) {
                    formatted = getFormattedNumber(lastNonSeparator, hasCursor)
                    hasCursor = false
                }
                lastNonSeparator = char
            }
            if (index == curIndex) {
                hasCursor = true
            }
        }

        if (lastNonSeparator.code != 0) {
            formatted = getFormattedNumber(lastNonSeparator, hasCursor)
        }
        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0
        formatted?.forEachIndexed { index, char ->
            if (!PhoneNumberUtils.isNonSeparator(char)) {
                specialCharsCount++
                transformedToOriginal.add(index - specialCharsCount)
            } else {
                originalToTransformed.add(index)
                transformedToOriginal.add(index - specialCharsCount)
            }
        }
        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation(formatted, originalToTransformed, transformedToOriginal)
    }

    private fun getFormattedNumber(lastNonSeparator: Char, hasCursor: Boolean): String? {
        return if (hasCursor) {
            phoneNumberFormatter.inputDigitAndRememberPosition(lastNonSeparator)
        } else {
            phoneNumberFormatter.inputDigit(lastNonSeparator)
        }
    }

    private data class Transformation(
        val formatted: String?,
        val originalToTransformed: List<Int>,
        val transformedToOriginal: List<Int>,
    )
}

@Composable
fun rememberPhoneNumberVisualTransformation(
    context: Context = LocalContext.current,
): PhoneNumberVisualTransformation {
    return remember(context) {
        PhoneNumberVisualTransformation(context)
    }
}
