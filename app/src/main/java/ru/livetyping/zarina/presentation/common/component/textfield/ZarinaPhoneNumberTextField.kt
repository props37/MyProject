package ru.livetyping.zarina.presentation.common.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.rememberPhoneNumberVisualTransformation

@Composable
fun ZarinaPhoneNumberTextField(
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Large,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: String = stringResource(R.string.phone),
    placeholder: String = stringResource(R.string.phone_text_field_placeholder),
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = remember {
        KeyboardOptions(keyboardType = KeyboardType.Phone)
    },
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = rememberPhoneNumberVisualTransformation(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(UiKitTheme.colors.text.general.regular.default),
) {
    var selection by remember { mutableStateOf(TextRange(phoneNumber.length)) }
    val textFieldValue by remember(phoneNumber, selection) {
        mutableStateOf(TextFieldValue(phoneNumber, selection))
    }

    ZarinaTextField(
        textFieldValue = textFieldValue,
        onValueChanged = {
            onPhoneNumberChanged(it.text)
            selection = it.selection
        },
        isEnabled = isEnabled,
        isError = isError,
        isReadOnly = isReadOnly,
        size = size,
        textStyle = textStyle,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingContent = leadingContent,
        innerTrailingContent = innerTrailingContent,
        outerTrailingContent = outerTrailingContent,
        description = description,
        colors = colors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var phone by remember { mutableStateOf("") }

        ZarinaPhoneNumberTextField(
            phoneNumber = phone,
            onPhoneNumberChanged = { phone = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
