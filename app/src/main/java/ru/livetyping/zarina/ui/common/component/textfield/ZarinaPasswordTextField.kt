package ru.livetyping.zarina.ui.common.component.textfield

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

@Composable
fun ZarinaPasswordTextField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Large,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: String = stringResource(R.string.password),
    placeholder: String = stringResource(R.string.password_text_field_placeholder_eight_symbolds),
    leadingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = remember {
        KeyboardOptions(keyboardType = KeyboardType.Password)
    },
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(UiKitTheme.colors.text.general.regular.default),
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
) {
    var isPasswordHidden by remember { mutableStateOf(true) }
    val visualTransformation = remember(isPasswordHidden) {
        if (isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None
    }

    ZarinaTextField(
        value = password,
        onValueChanged = onPasswordChanged,
        isEnabled = isEnabled,
        isError = isError,
        isReadOnly = isReadOnly,
        size = size,
        textStyle = textStyle,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingContent = leadingContent,
        innerTrailingContent = {
            AnimatedContent(
                targetState = isPasswordHidden,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
                },
                contentAlignment = Alignment.Center,
                label = "PasswordTextField eye icon",
            ) { isPasswordHiddenValue ->
                ZarinaIconButton(
                    onClick = { isPasswordHidden = !isPasswordHidden },
                    indication = rememberRipple(bounded = false, radius = 16.dp),
                    modifier = Modifier.size(36.dp),
                ) {
                    val iconResId: Int
                    val contentDescriptionResId: Int
                    if (isPasswordHiddenValue) {
                        iconResId = R.drawable.ic_eye_open_24
                        contentDescriptionResId = R.string.show_password
                    } else {
                        iconResId = R.drawable.ic_eye_closed_24
                        contentDescriptionResId = R.string.hide_password
                    }

                    Icon(
                        painter = painterResource(iconResId),
                        contentDescription = stringResource(contentDescriptionResId),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        },
        outerTrailingContent = outerTrailingContent,
        description = description,
        colors = colors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        backgroundColor = backgroundColor,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var password by remember { mutableStateOf("") }

        ZarinaPasswordTextField(
            password = password,
            onPasswordChanged = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
