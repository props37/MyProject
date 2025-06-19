package ru.livetyping.zarina.core.uikit.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.text.UppercaseOutputTransformation
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDividerDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

// TODO: [Medium] Add label animation

@Composable
public fun ZarinaTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Medium,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = UppercaseOutputTransformation,
    scrollState: ScrollState = rememberScrollState(),
) {
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        state = state,
        enabled = isEnabled,
        readOnly = isReadOnly,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = lineLimits,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = remember(colors.cursorColor) { SolidColor(colors.cursorColor) },
        outputTransformation = outputTransformation,
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
                innerTrailingContent = innerTrailingContent,
                outerTrailingContent = outerTrailingContent,
                description = description,
                colors = colors,
            )
        },
        scrollState = scrollState,
        modifier = modifier
            .drawBehind { drawRect(colors.backgroundColor) }
            .onFocusChanged { focusState = it },
    )
}

@Composable
public fun ZarinaTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Medium,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: ZarinaTextFieldColors = ZarinaTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
) {
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        enabled = isEnabled,
        readOnly = isReadOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = remember(colors.cursorColor) { SolidColor(colors.cursorColor) },
        decorationBox = { innerTextField ->
            ZarinaTextFieldDecoration(
                value = value,
                isEnabled = isEnabled,
                isError = isError,
                focusState = focusState,
                textStyle = textStyle,
                size = size,
                innerTextField = innerTextField,
                label = label,
                placeholder = placeholder,
                leadingContent = leadingContent,
                innerTrailingContent = innerTrailingContent,
                outerTrailingContent = outerTrailingContent,
                description = description,
                colors = colors,
            )
        },
        modifier = modifier
            .drawBehind { drawRect(colors.backgroundColor) }
            .onFocusChanged { focusState = it },
    )
}

@Composable
internal fun ZarinaTextFieldDecoration(
    value: CharSequence,
    isEnabled: Boolean,
    isError: Boolean,
    focusState: FocusState?,
    textStyle: TextStyle,
    size: ZarinaTextFieldSize,
    innerTextField: @Composable () -> Unit,
    label: (@Composable () -> Unit)?,
    placeholder: (@Composable () -> Unit)?,
    leadingContent: (@Composable () -> Unit)?,
    innerTrailingContent: (@Composable () -> Unit)?,
    outerTrailingContent: (@Composable () -> Unit)?,
    description: (@Composable () -> Unit)?,
    colors: ZarinaTextFieldColors,
) {
    Column {
        label?.let { label ->
            val labelTextStyle = ZarinaTextFieldDefaults.labelTextStyleFromSize(size)
            val padding = ZarinaTextFieldDefaults.labelPaddingFromSize(size)
            val labelColor by animateColorAsState(
                targetValue = colors.getLabelColor(isEnabled),
                label = "Label color",
            )

            CompositionLocalProvider(
                LocalTextStyle provides labelTextStyle,
                LocalContentColor provides labelColor,
            ) {
                label()
            }
            Spacer(modifier = Modifier.height(padding))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val indicationLineColor = animateColorAsState(
                targetValue = colors.getIndicationLineColor(
                    isEnabled = isEnabled,
                    isActive = focusState?.isFocused == true,
                    isError = isError,
                ),
                label = "Indication line color",
            )
            val indicationLinePadding = ZarinaTextFieldDefaults.indicationLinePaddingFromSize(size)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        val width = ZarinaDividerDefaults.Thickness.toPx()
                        drawLine(
                            color = indicationLineColor.value,
                            start = Offset(0f, this.size.height - width),
                            end = Offset(this.size.width, this.size.height - width),
                            strokeWidth = width,
                        )
                    }
                    .padding(bottom = indicationLinePadding),
            ) {
                leadingContent?.let { content ->
                    val leadingContentColor by animateColorAsState(
                        targetValue = colors.getLeadingContentColor(isEnabled),
                        label = "Leading content color",
                    )

                    CompositionLocalProvider(LocalContentColor provides leadingContentColor) {
                        content()
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Box(modifier = Modifier.weight(1f)) {
                    val textColor by animateColorAsState(
                        targetValue = colors.getTextColor(isEnabled),
                        label = "Text color",
                    )

                    CompositionLocalProvider(LocalContentColor provides textColor) {
                        innerTextField()
                    }

                    if (value.isEmpty() && placeholder != null) {
                        val placeholderColor by animateColorAsState(
                            targetValue = colors.getPlaceholderColor(isEnabled),
                            label = "Placeholder color",
                        )

                        CompositionLocalProvider(
                            LocalTextStyle provides textStyle,
                            LocalContentColor provides placeholderColor,
                        ) {
                            placeholder()
                        }
                    }
                }

                innerTrailingContent?.let { content ->
                    val innerTrailingContentColor by animateColorAsState(
                        targetValue = colors.getInnerTrailingContentColor(isEnabled),
                        label = "Inner trailing content color",
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    CompositionLocalProvider(LocalContentColor provides innerTrailingContentColor) {
                        content()
                    }
                }
            }

            outerTrailingContent?.let { content ->
                Spacer(modifier = Modifier.width(8.dp))

                val outerTrailingContentColor by animateColorAsState(
                    targetValue = colors.getOuterTrailingContentColor(isEnabled),
                    label = "Outer trailing content color",
                )

                CompositionLocalProvider(
                    LocalTextStyle provides UiKitTheme2.typography.body2,
                    LocalContentColor provides outerTrailingContentColor,
                ) {
                    content()
                }
            }
        }

        description?.let { description ->
            val padding = ZarinaTextFieldDefaults.descriptionPaddingFromSize(size)

            Spacer(modifier = Modifier.height(padding))

            val descriptionTextStyle =
                ZarinaTextFieldDefaults.descriptionTextStyleFromSize(size)
            val descriptionColor by animateColorAsState(
                targetValue = colors.getDescriptionColor(isEnabled, isError),
                label = "Description color",
            )

            CompositionLocalProvider(
                LocalTextStyle provides descriptionTextStyle,
                LocalContentColor provides descriptionColor,
            ) {
                description()
            }
        }
    }
}

public data class ZarinaTextFieldColors(
    val backgroundColor: Color,
    val textColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val leadingContentColor: Color,
    val innerTrailingContentColor: Color,
    val outerTrailingContentColor: Color,
    val descriptionColor: Color,
    val indicationLineColor: Color,
    val activeIndicationLineColor: Color,
    val errorIndicationLineColor: Color,
    val errorDescriptionColor: Color,
    val disabledTextColor: Color,
    val disabledPlaceholderColor: Color,
    val disabledLabelColor: Color,
    val disabledLeadingContentColor: Color,
    val disabledInnerTrailingContentColor: Color,
    val disabledOuterTrailingContentColor: Color,
    val disabledDescriptionColor: Color,
    val disabledIndicationLineColor: Color,
    val disabledErrorIndicationLineColor: Color,
    val cursorColor: Color,
) {
    public fun getTextColor(isEnabled: Boolean): Color =
        if (isEnabled) textColor else disabledTextColor

    public fun getPlaceholderColor(isEnabled: Boolean): Color =
        if (isEnabled) placeholderColor else disabledPlaceholderColor

    public fun getLabelColor(isEnabled: Boolean): Color =
        if (isEnabled) labelColor else disabledLabelColor

    public fun getLeadingContentColor(isEnabled: Boolean): Color =
        if (isEnabled) leadingContentColor else disabledLeadingContentColor

    public fun getInnerTrailingContentColor(isEnabled: Boolean): Color =
        if (isEnabled) innerTrailingContentColor else disabledInnerTrailingContentColor

    public fun getOuterTrailingContentColor(isEnabled: Boolean): Color =
        if (isEnabled) outerTrailingContentColor else disabledOuterTrailingContentColor

    public fun getDescriptionColor(isEnabled: Boolean, isError: Boolean): Color = when {
        isError -> errorDescriptionColor
        isEnabled -> descriptionColor
        else -> disabledDescriptionColor
    }

    public fun getIndicationLineColor(
        isEnabled: Boolean,
        isActive: Boolean,
        isError: Boolean,
    ): Color = when {
        isError && isEnabled -> errorIndicationLineColor
        isError -> disabledErrorIndicationLineColor
        isActive -> activeIndicationLineColor
        isEnabled -> indicationLineColor
        else -> disabledIndicationLineColor
    }
}

public enum class ZarinaTextFieldSize { Medium }

public object ZarinaTextFieldDefaults {
    private val IconSize: Dp get() = 16.dp

    @Composable
    public fun AppearingLabel(
        textFieldValue: String,
        label: String,
        modifier: Modifier = Modifier,
    ) {
        val labelValue = if (textFieldValue.isNotBlank()) label else ""

        Text(
            text = labelValue.uppercase(),
            modifier = modifier,
        )
    }

    @Composable
    public fun ClearButton(
        isVisible: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        iconSize: Dp = IconSize,
        indication: Indication? = ripple(bounded = false, radius = 8.dp),
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = AnimatedContentDefaultEnterTransition,
            exit = AnimatedContentDefaultExitTransition,
            modifier = modifier,
        ) {
            ClearButtonImpl(
                onClick = onClick,
                iconSize = iconSize,
                indication = indication,
            )
        }
    }

    @Composable
    public fun CancelButton(
        isVisible: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AnimatedContent(
            targetState = isVisible,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.Center,
            label = "Cancel button",
            modifier = modifier,
        ) {
            if (it) {
                CancelButtonImpl(onClick = onClick)
            }
        }
    }

    @Composable
    public fun colors(
        backgroundColor: Color = UiKitTheme2.colors.white,
        textColor: Color = UiKitTheme2.colors.mainBlack,
        placeholderColor: Color = UiKitTheme2.colors.middleGray,
        labelColor: Color = UiKitTheme2.colors.middleGray,
        leadingContentColor: Color = UiKitTheme2.colors.middleGray,
        innerTrailingContentColor: Color = UiKitTheme2.colors.middleGray,
        outerTrailingContentColor: Color = UiKitTheme2.colors.middleGray,
        descriptionColor: Color = UiKitTheme2.colors.middleGray,
        indicationLineColor: Color = UiKitTheme2.colors.lightGray,
        activeIndicationLineColor: Color = UiKitTheme2.colors.mainBlack,
        errorIndicationLineColor: Color = UiKitTheme2.colors.error,
        errorDescriptionColor: Color = UiKitTheme2.colors.error,
        disabledTextColor: Color = textColor,
        disabledPlaceholderColor: Color = placeholderColor,
        disabledLabelColor: Color = labelColor,
        disabledLeadingContentColor: Color = leadingContentColor,
        disabledInnerTrailingContentColor: Color = innerTrailingContentColor,
        disabledOuterTrailingContentColor: Color = outerTrailingContentColor,
        disabledDescriptionColor: Color = descriptionColor,
        disabledIndicationLineColor: Color = indicationLineColor,
        disabledErrorIndicationLineColor: Color = errorIndicationLineColor,
        cursorColor: Color = UiKitTheme2.colors.mainBlack,
    ): ZarinaTextFieldColors = ZarinaTextFieldColors(
        backgroundColor = backgroundColor,
        textColor = textColor,
        placeholderColor = placeholderColor,
        labelColor = labelColor,
        leadingContentColor = leadingContentColor,
        innerTrailingContentColor = innerTrailingContentColor,
        outerTrailingContentColor = outerTrailingContentColor,
        descriptionColor = descriptionColor,
        indicationLineColor = indicationLineColor,
        activeIndicationLineColor = activeIndicationLineColor,
        errorIndicationLineColor = errorIndicationLineColor,
        errorDescriptionColor = errorDescriptionColor,
        disabledTextColor = disabledTextColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        disabledLabelColor = disabledLabelColor,
        disabledLeadingContentColor = disabledLeadingContentColor,
        disabledInnerTrailingContentColor = disabledInnerTrailingContentColor,
        disabledOuterTrailingContentColor = disabledOuterTrailingContentColor,
        disabledDescriptionColor = disabledDescriptionColor,
        disabledIndicationLineColor = disabledIndicationLineColor,
        disabledErrorIndicationLineColor = disabledErrorIndicationLineColor,
        cursorColor = cursorColor,
    )

    @Composable
    public fun colorsIgnoringDisabled(
        baseColors: ZarinaTextFieldColors = colors(),
    ): ZarinaTextFieldColors {
        return baseColors.copy(
            disabledTextColor = baseColors.textColor,
            disabledPlaceholderColor = baseColors.placeholderColor,
            disabledLabelColor = baseColors.labelColor,
            disabledLeadingContentColor = baseColors.leadingContentColor,
            disabledInnerTrailingContentColor = baseColors.innerTrailingContentColor,
            disabledOuterTrailingContentColor = baseColors.outerTrailingContentColor,
            disabledDescriptionColor = baseColors.descriptionColor,
            disabledIndicationLineColor = baseColors.indicationLineColor,
            disabledErrorIndicationLineColor = baseColors.errorIndicationLineColor,
        )
    }

    @Composable
    public fun textStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Medium -> UiKitTheme2.typography.body
    }

    @Composable
    public fun labelTextStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Medium -> UiKitTheme2.typography.body2
    }

    @Composable
    public fun descriptionTextStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Medium -> UiKitTheme2.typography.body2
    }

    @Stable
    public fun labelPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Medium -> 10.dp
    }

    @Stable
    public fun indicationLinePaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Medium -> 8.dp
    }

    @Stable
    public fun descriptionPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Medium -> 8.dp
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ClearButtonImpl(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        iconSize: Dp = IconSize,
        indication: Indication? = ripple(bounded = false, radius = 8.dp),
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            ZarinaIconButton(
                onClick = onClick,
                indication = indication,
                modifier = modifier,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cross_shaped_24),
                    contentDescription = stringResource(R.string.res_clear),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CancelButtonImpl(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            ZarinaButton(
                onClick = onClick,
                size = ZarinaButtonSize.Small,
                colors = ZarinaButtonDefaults.backlessColors(),
                modifier = modifier.heightIn(min = 36.dp),
            ) {
                Text(
                    text = stringResource(R.string.res_cancel).uppercase(),
                    style = UiKitTheme2.typography.body2,
                )
            }
        }
    }
}
