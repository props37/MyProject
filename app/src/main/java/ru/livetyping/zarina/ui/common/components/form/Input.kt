package ru.livetyping.zarina.ui.common.components.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    error: String? = null,
    isEnabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        textStyle = UiKitTheme.typographyOld.circle1718.copy(color = UiKitTheme.colorsOld.primaryContentColor),
        singleLine = true,
        enabled = isEnabled,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                label = {
                    Text(
                        text = hint,
                    )
                },
                supportingText = {
                    Text(
                        text = error.orEmpty(),
                        modifier = Modifier.graphicsLayer { translationX = -16.dp.toPx() }
                    )
                },
                shape = TextFieldDefaults.filledShape,
                singleLine = true,
                enabled = isEnabled,
                isError = isError,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = UiKitTheme.colorsOld.primaryContentColor,
                    unfocusedTextColor = UiKitTheme.colorsOld.primaryContentColor,
                    disabledTextColor = UiKitTheme.colorsOld.primaryContentColor,
                    focusedContainerColor = UiKitTheme.colorsOld.screenBackground,
                    unfocusedContainerColor = UiKitTheme.colorsOld.screenBackground,
                    cursorColor = UiKitTheme.colorsOld.primaryContentColor,
                    focusedIndicatorColor = UiKitTheme.colorsOld.listDivider,
                    unfocusedIndicatorColor = UiKitTheme.colorsOld.listDivider,
                    errorIndicatorColor = UiKitTheme.colorsOld.error,
                    focusedLabelColor = UiKitTheme.colorsOld.hint,
                    unfocusedLabelColor = UiKitTheme.colorsOld.hint,
                    disabledLabelColor = UiKitTheme.colorsOld.hint,
                    errorLabelColor = UiKitTheme.colorsOld.error,
                    focusedSupportingTextColor = UiKitTheme.colorsOld.listDivider,
                    unfocusedSupportingTextColor = UiKitTheme.colorsOld.listDivider,
                    errorSupportingTextColor = UiKitTheme.colorsOld.error,
                ),
                contentPadding = TextFieldDefaults.textFieldWithLabelPadding(
                    start = 0.dp,
                    end = 0.dp
                ),
            )
        },
        modifier = modifier,
    )
}
