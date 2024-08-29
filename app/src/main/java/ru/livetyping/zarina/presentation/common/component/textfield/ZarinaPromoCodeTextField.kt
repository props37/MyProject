package ru.livetyping.zarina.presentation.common.component.textfield

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec

@Composable
fun ZarinaPromoCodeTextField(
    promoCode: String,
    onPromoCodeChanged: (String) -> Unit,
    isApplied: Boolean,
    onApplyClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: String = stringResource(R.string.promo_code),
    placeholder: String = stringResource(R.string.promo_code),
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = {
        ZarinaPromoCodeTextFieldDefaults.InnerTrailingContent(
            promoCode = promoCode,
            isApplied = isApplied,
            onClearClicked = onRemoveClicked,
        )
    },
    outerTrailingContent: (@Composable () -> Unit)? = {
        ZarinaPromoCodeTextFieldDefaults.ApplyButton(
            isVisible = promoCode.isNotBlank() && !isApplied,
            onClick = onApplyClicked,
        )
    },
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = remember {
        KeyboardOptions(keyboardType = KeyboardType.Text)
    },
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(UiKitTheme.colors.text.general.regular.default),
) {
    ZarinaTextField(
        value = promoCode,
        onValueChanged = onPromoCodeChanged,
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

@Composable
fun ZarinaPromoCodeTextField(
    state: TextFieldState,
    isApplied: Boolean,
    onApplyClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: String = stringResource(R.string.promo_code),
    placeholder: String = stringResource(R.string.promo_code),
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = {
        ZarinaPromoCodeTextFieldDefaults.InnerTrailingContent(
            promoCode = state.text.toString(),
            isApplied = isApplied,
            onClearClicked = onRemoveClicked,
        )
    },
    outerTrailingContent: (@Composable () -> Unit)? = {
        ZarinaPromoCodeTextFieldDefaults.ApplyButton(
            isVisible = state.text.toString().isNotBlank() && !isApplied,
            onClick = onApplyClicked,
        )
    },
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = remember {
        KeyboardOptions(keyboardType = KeyboardType.Text)
    },
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
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
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
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

object ZarinaPromoCodeTextFieldDefaults {

    @Composable
    fun InnerTrailingContent(
        promoCode: String,
        isApplied: Boolean,
        onClearClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            AnimatedVisibility(
                visible = isApplied,
                enter = AnimatedContentDefaultEnterTransition,
                exit = AnimatedContentDefaultExitTransition,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_checkmark_24),
                    contentDescription = stringResource(R.string.promo_code_is_applied),
                    tint = UiKitTheme.colors.icon.regular.default,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(16.dp),
                )
            }

            ZarinaTextFieldDefaults.ClearButton(
                isVisible = promoCode.isNotBlank(),
                onClick = onClearClicked,
            )
        }
    }

    @Composable
    fun ApplyButton(
        isVisible: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        AnimatedContent(
            targetState = isVisible,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.Center,
            label = "ApplyButton",
        ) { isVisible ->
            if (isVisible) {
                ZarinaButton(
                    onClick = onClick,
                    size = ZarinaButtonSize.Small,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    modifier = modifier,
                ) {
                    Text(
                        text = stringResource(R.string.apply).uppercase(),
                        style = UiKitTheme.typography.caption1.regular,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var promoCode by remember { mutableStateOf("") }
        var isApplied by remember { mutableStateOf(false) }

        ZarinaPromoCodeTextField(
            promoCode = promoCode,
            onPromoCodeChanged = { promoCode = it },
            isApplied = isApplied,
            onApplyClicked = { isApplied = true },
            onRemoveClicked = { isApplied = false },
        )
    }
}
