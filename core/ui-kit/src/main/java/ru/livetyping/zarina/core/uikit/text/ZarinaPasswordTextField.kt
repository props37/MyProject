package ru.livetyping.zarina.core.uikit.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaPasswordTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: (@Composable () -> Unit)? = {
        val labelResId = if (state.text.isNotEmpty()) {
            stringResource(RCommon.string.res_password)
        } else ""

        Text(text = labelResId)
    },
    placeholder: (@Composable () -> Unit)? = {
        Text(text = stringResource(RCommon.string.res_password))
    },
    leadingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = ZarinaPasswordTextFieldDefaults.KeyboardOptions,
    onKeyboardAction: KeyboardActionHandler? = null,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val isPasswordVisibilityButtonVisible = state.text.isNotEmpty()
    var isPasswordHidden by remember { mutableStateOf(true) }
    val obfuscationMode by remember {
        derivedStateOf {
            if (isPasswordHidden) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible
        }
    }

    var focusState by remember { mutableStateOf<FocusState?>(null) }

    DisposableEffect(state.text.isNotEmpty()) {
        isPasswordHidden = true
        onDispose {}
    }

    BasicSecureTextField(
        state = state,
        enabled = isEnabled,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = remember(colors.cursorColor) { SolidColor(colors.cursorColor) },
        decorator = { innerTextField ->
            ZarinaTextFieldDecoration(
                value = state.text,
                isEnabled = isEnabled,
                isError = isError,
                focusState = focusState,
                textStyle = textStyle,
                size = size,
                innerTextField = innerTextField,
                label = label,
                placeholder = placeholder,
                leadingContent = leadingContent,
                innerTrailingContent = {
                    val alpha = animateFloatAsState(
                        targetValue = if (isPasswordVisibilityButtonVisible) 1f else 0f,
                        label = "PasswordVisibilityButton alpha",
                    )

                    PasswordVisibilityButton(
                        isPasswordHidden = isPasswordHidden,
                        onPasswordHiddenChanged = { isPasswordHidden = it },
                        isEnabled = isPasswordVisibilityButtonVisible,
                        modifier = Modifier.graphicsLayer {
                            this.alpha = alpha.value
                        },
                    )
                },
                outerTrailingContent = outerTrailingContent,
                description = description,
                colors = colors,
            )
        },
        textObfuscationMode = obfuscationMode,
        modifier = modifier
            .background(colors.backgroundColor)
            .onFocusChanged { focusState = it },
    )
}

@Composable
private fun PasswordVisibilityButton(
    isPasswordHidden: Boolean,
    onPasswordHiddenChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    val iconSize = 16.dp

    ZarinaIconButton(
        onClick = { onPasswordHiddenChanged(!isPasswordHidden) },
        indication = ripple(bounded = false, radius = iconSize),
        isEnabled = isEnabled,
        modifier = modifier.size(36.dp),
    ) {
        AnimatedContent(
            targetState = isPasswordHidden,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(sizeTransform = null)
            },
            contentAlignment = Alignment.Center,
            label = "PasswordVisibilityButton",
        ) { isPasswordHidden ->
            val iconResId: Int
            val contentDescriptionResId: Int
            if (isPasswordHidden) {
                iconResId = RCommon.drawable.ic_eye_open_24
                contentDescriptionResId = R.string.uikit_show_password
            } else {
                iconResId = RCommon.drawable.ic_eye_closed_24
                contentDescriptionResId = R.string.uikit_hide_password
            }

            Icon(
                imageVector = ImageVector.vectorResource(iconResId),
                contentDescription = stringResource(contentDescriptionResId),
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

public object ZarinaPasswordTextFieldDefaults {
    public val KeyboardOptions: KeyboardOptions
        get() = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
        )
}
