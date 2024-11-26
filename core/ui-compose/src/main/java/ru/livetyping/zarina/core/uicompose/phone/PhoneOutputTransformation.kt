package ru.livetyping.zarina.core.uicompose.phone

import android.content.Context
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.livetyping.zarina.core.kotlinutil.LocaleUtil
import ru.livetyping.zarina.core.uicommon.PhoneNumberUtilProvider

internal class PhoneOutputTransformation(
    context: Context,
    countryCode: String,
) : OutputTransformation {
    private val phoneNumberUtil = PhoneNumberUtilProvider.provide(context)
    private val phoneFormatter = phoneNumberUtil.getAsYouTypeFormatter(countryCode)

    override fun TextFieldBuffer.transformOutput() {
        val buffer = this
        val inputChatSequence = buffer.asCharSequence()

        phoneFormatter.clear()
        var formattedString = ""
        inputChatSequence.forEachIndexed { index, c ->
            if (index == inputChatSequence.lastIndex) {
                formattedString = phoneFormatter.inputDigit(c)
            } else {
                phoneFormatter.inputDigit(c)
            }
        }

        var inputIndex = 0
        var formattedIndex = 0
        while (formattedIndex <= formattedString.lastIndex) {
            val inputChar = inputChatSequence.getOrNull(inputIndex)
            val formattedChar = formattedString[formattedIndex]

            if (inputChar != formattedChar) {
                buffer.insert(inputIndex, formattedChar.toString())
                inputIndex++
                formattedIndex++
            } else {
                inputIndex++
                formattedIndex++
            }
        }
    }
}

@Composable
public fun rememberPhoneOutputTransformation(
    context: Context = LocalContext.current,
    countryCode: String = CountryCodeRu,
): OutputTransformation {
    return remember(context, countryCode) {
        PhoneOutputTransformation(context, countryCode)
    }
}

private val CountryCodeRu: String get() = LocaleUtil.RU.country
