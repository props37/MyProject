package ru.livetyping.zarina.presentation.common.component.textfield

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.text.textAsFlow

// TODO: [Medium] Add label animation

@Composable
fun ZarinaTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    onTextChanged: ((String) -> Unit)? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
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
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
) {
    val updatedOnTextChanged by rememberUpdatedState(onTextChanged)
    LaunchedEffect(state) {
        state.textAsFlow()
            .onEach { updatedOnTextChanged?.invoke(it.toString()) }
            .launchIn(this)
    }

    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        state = state,
        modifier = modifier
            .background(colors.backgroundColor)
            .onFocusChanged { focusState = it },
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
            Decoration(
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
    )
}

@Composable
fun ZarinaTextField(
    textFieldValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
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
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(UiKitTheme.colors.text.general.regular.default),
) {
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChanged,
        modifier = modifier
            .background(colors.backgroundColor)
            .onFocusChanged { focusState = it },
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
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            Decoration(
                value = textFieldValue.text,
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
    )
}

@Composable
fun ZarinaTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Small,
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
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(UiKitTheme.colors.text.general.regular.default),
) {
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier
            .background(colors.backgroundColor)
            .onFocusChanged { focusState = it },
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
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            Decoration(
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
    )
}

@Composable
private fun Decoration(
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
            val verticalPadding = ZarinaTextFieldDefaults.textVerticalPaddingFromSize(size)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        val width = 0.5.dp.toPx()
                        drawLine(
                            color = indicationLineColor.value,
                            start = Offset(0f, this.size.height - width),
                            end = Offset(this.size.width, this.size.height - width),
                            strokeWidth = width,
                        )
                    }
                    .padding(vertical = verticalPadding),
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
                    LocalTextStyle provides UiKitTheme.typography.caption1.regular,
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

data class ZarinaTextFieldColors(
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
    fun getTextColor(isEnabled: Boolean): Color = if (isEnabled) textColor else disabledTextColor

    fun getPlaceholderColor(isEnabled: Boolean): Color =
        if (isEnabled) placeholderColor else disabledPlaceholderColor

    fun getLabelColor(isEnabled: Boolean): Color = if (isEnabled) labelColor else disabledLabelColor

    fun getLeadingContentColor(isEnabled: Boolean): Color =
        if (isEnabled) leadingContentColor else disabledLeadingContentColor

    fun getInnerTrailingContentColor(isEnabled: Boolean): Color =
        if (isEnabled) innerTrailingContentColor else disabledInnerTrailingContentColor

    fun getOuterTrailingContentColor(isEnabled: Boolean): Color =
        if (isEnabled) outerTrailingContentColor else disabledOuterTrailingContentColor

    fun getDescriptionColor(isEnabled: Boolean, isError: Boolean): Color = when {
        isError -> errorDescriptionColor
        isEnabled -> descriptionColor
        else -> disabledDescriptionColor
    }

    fun getIndicationLineColor(
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

enum class ZarinaTextFieldSize { Small }

object ZarinaTextFieldDefaults {
    val IconSizeSmall: Dp get() = 16.dp

    @Composable
    fun AppearingLabel(
        textFieldValue: CharSequence,
        label: String,
        modifier: Modifier = Modifier,
    ) {
        val labelValue = if (textFieldValue.isNotBlank()) label else ""

        Text(
            text = labelValue,
            modifier = modifier,
        )
    }

    @Composable
    fun ClearButton(
        isVisible: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        iconSize: Dp = IconSizeSmall,
        indication: Indication? = ripple(bounded = false, radius = 8.dp),
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = remember { AnimatedContentDefaultEnterTransition },
            exit = remember { AnimatedContentDefaultExitTransition },
            modifier = modifier,
        ) {
            ClearButtonImpl(
                onClick = onClick,
                iconSize = iconSize,
                indication = indication,
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ClearButtonImpl(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        iconSize: Dp = IconSizeSmall,
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
                    contentDescription = stringResource(R.string.clear),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }

    @Composable
    fun CancelButton(
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
                CancelButton(onClick = onClick)
            }
        }
    }

    @Composable
    private fun CancelButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Small,
            colors = ZarinaButtonDefaults.backlessColors(),
            modifier = modifier,
        ) {
            Text(
                text = stringResource(R.string.cancel).uppercase(),
                style = UiKitTheme.typography.caption1.regular,
            )
        }
    }

    @Composable
    fun colors(
        backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
        textColor: Color = UiKitTheme.colors.text.general.regular.default,
        placeholderColor: Color = UiKitTheme.colors.text.general.regular.muted,
        labelColor: Color = UiKitTheme.colors.text.general.regular.muted,
        leadingContentColor: Color = UiKitTheme.colors.icon.regular.muted,
        innerTrailingContentColor: Color = UiKitTheme.colors.icon.regular.default,
        outerTrailingContentColor: Color = UiKitTheme.colors.text.button.outline.default, // TODO: [Low] Change to button-cell-default
        descriptionColor: Color = UiKitTheme.colors.text.general.regular.muted,
        indicationLineColor: Color = UiKitTheme.colors.border.general.default,
        activeIndicationLineColor: Color = UiKitTheme.colors.border.general.active,
        errorIndicationLineColor: Color = UiKitTheme.colors.border.general.error,
        errorDescriptionColor: Color = UiKitTheme.colors.text.general.accent.red,
        disabledTextColor: Color = UiKitTheme.colors.text.general.regular.disabled,
        disabledPlaceholderColor: Color = UiKitTheme.colors.text.general.regular.disabled,
        disabledLabelColor: Color = UiKitTheme.colors.text.general.regular.disabled,
        disabledLeadingContentColor: Color = UiKitTheme.colors.icon.regular.disabled,
        disabledInnerTrailingContentColor: Color = UiKitTheme.colors.icon.regular.disabled,
        disabledOuterTrailingContentColor: Color = UiKitTheme.colors.text.button.outline.disabled, // TODO: [Low] Change to button-cell-disabled
        disabledDescriptionColor: Color = UiKitTheme.colors.text.general.regular.disabled,
        disabledIndicationLineColor: Color = UiKitTheme.colors.border.general.disabled,
        disabledErrorIndicationLineColor: Color = UiKitTheme.colors.border.general.errorDisabled,
        cursorColor: Color = UiKitTheme.colors.text.general.regular.default,
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
    fun colorsIgnoringDisabled(
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
    fun textStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Small -> UiKitTheme.typography.secondary.light
    }

    @Composable
    fun labelTextStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Small -> UiKitTheme.typography.footnote.light
    }

    @Composable
    fun descriptionTextStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Small -> UiKitTheme.typography.footnote.light
    }

    @Stable
    fun textVerticalPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Small -> 6.dp
    }

    @Stable
    fun labelPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Small -> 2.dp
    }

    @Stable
    fun descriptionPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Small -> 8.dp
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            val state = rememberTextFieldState()
            val isError by remember {
                derivedStateOf { state.text.contains("error") }
            }
            val isEnabled by remember {
                derivedStateOf { !state.text.contains("dis") }
            }

            ZarinaTextField(
                state = state,
                isEnabled = isEnabled,
                isError = isError,
                label = {
                    Text(text = "Label")
                },
                placeholder = {
                    Text(text = "Placeholder")
                },
                leadingContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                },
                innerTrailingContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_cross_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { state.clearText() },
                    )
                },
                outerTrailingContent = {
                    Text(
                        text = "Button".uppercase(),
                        color = UiKitTheme.colors.text.button.secondary.default,
                    )
                },
                description = {
                    Text(text = "Description")
                },
            )
        }
    }
}
