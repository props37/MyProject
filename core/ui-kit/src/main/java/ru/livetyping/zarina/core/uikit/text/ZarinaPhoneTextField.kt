package ru.livetyping.zarina.core.uikit.text

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.uicompose.phone.rememberPhoneOutputTransformation
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaPhoneTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
    inputTransformation: InputTransformation? = ZarinaPhoneTextFieldDefaults.InputTransformationDefault,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: (@Composable () -> Unit)? = {
        val text = if (state.text.isNotEmpty()) {
            stringResource(RCommon.string.res_phone)
        } else ""

        Text(text = text)
    },
    placeholder: (@Composable () -> Unit)? = {
        Text(text = stringResource(RCommon.string.res_phone))
    },
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = remember { ZarinaPhoneTextFieldDefaults.KeyboardOptions },
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = rememberPhoneOutputTransformation(),
    scrollState: ScrollState = rememberScrollState(),
) {
    ZarinaTextField(
        state = state,
        isEnabled = isEnabled,
        isError = isError,
        isReadOnly = isReadOnly,
        size = size,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingContent = leadingContent,
        innerTrailingContent = innerTrailingContent,
        outerTrailingContent = outerTrailingContent,
        description = description,
        colors = colors,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        outputTransformation = outputTransformation,
        scrollState = scrollState,
        modifier = modifier,
    )
}

public object ZarinaPhoneTextFieldDefaults {
    public val KeyboardOptions: KeyboardOptions
        get() = KeyboardOptions(keyboardType = KeyboardType.Phone)

    internal val InputTransformationDefault = InputTransformation
        .byValue { _, proposed ->
            proposed.filter { it.isDigit() || it == PLUS }
        }
        .maxLength(PhoneNumber.MAX_LENGTH)

    private const val PLUS = '+'
}
