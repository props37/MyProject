package ru.livetyping.zarina.core.uikit.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaPromoCodeTextField(
    state: TextFieldState,
    isApplied: Boolean,
    appliedPromoCode: String?,
    onApplyClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: String = stringResource(RCommon.string.res_promo_code),
    placeholder: String = stringResource(RCommon.string.res_promo_code),
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = {
        ZarinaPromoCodeTextFieldDefaults.InnerTrailingContent(
            promoCode = state.text.toString(),
            isApplied = isApplied,
            onClearClicked = onRemoveClicked,
        )
    },
    outerTrailingContent: (@Composable () -> Unit)? = {
        val text = state.text.toString()
        val isVisible = text.isNotBlank() && text != appliedPromoCode
        ZarinaPromoCodeTextFieldDefaults.ApplyButton(
            isVisible = isVisible,
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

public object ZarinaPromoCodeTextFieldDefaults {

    @Composable
    public fun InnerTrailingContent(
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
                    imageVector = ImageVector.vectorResource(RCommon.drawable.ic_checkmark_24),
                    contentDescription = stringResource(RCommon.string.res_promo_code_applied),
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
    public fun ApplyButton(
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
                        text = stringResource(RCommon.string.res_apply).uppercase(),
                        style = UiKitTheme.typography.caption1.regular,
                    )
                }
            }
        }
    }
}
