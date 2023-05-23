package ru.zarina.zarina.ui.common.components.form

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
import ru.zarina.zarina.ui.theme.UiKitTheme

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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        textStyle = UiKitTheme.typography.circle1718.copy(color = UiKitTheme.colors.primaryContentColor),
        singleLine = true,
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
                enabled = true,
                isError = isError,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = UiKitTheme.colors.primaryContentColor,
                    containerColor = UiKitTheme.colors.screenBackground,
                    cursorColor = UiKitTheme.colors.primaryContentColor,
                    focusedIndicatorColor = UiKitTheme.colors.listDivider,
                    unfocusedIndicatorColor = UiKitTheme.colors.listDivider,
                    errorIndicatorColor = UiKitTheme.colors.error,
                    focusedLabelColor = UiKitTheme.colors.hint,
                    unfocusedLabelColor = UiKitTheme.colors.hint,
                    errorLabelColor = UiKitTheme.colors.error,
                    focusedSupportingTextColor = UiKitTheme.colors.listDivider,
                    unfocusedSupportingTextColor = UiKitTheme.colors.listDivider,
                    errorSupportingTextColor = UiKitTheme.colors.error,
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
