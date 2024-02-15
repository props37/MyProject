package ru.zarina.zarina.ui.common.component.textfield

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Indication
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.IconButtonCustom
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

// TODO: [High] Apply error color to description
// TODO: [Low] Migrate to BasicTextField2

@Composable
fun ZarinaTextField(
    textFieldValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    readOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Large,
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(UiKitTheme.colorsReworked.text.general.regular.default),
) {
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChanged,
        modifier = modifier.onFocusChanged { focusState = it },
        enabled = isEnabled,
        readOnly = readOnly,
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
            DecorationBox(
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
    readOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Large,
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(UiKitTheme.colorsReworked.text.general.regular.default),
) {
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier.onFocusChanged { focusState = it },
        enabled = isEnabled,
        readOnly = readOnly,
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
            DecorationBox(
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
private fun DecorationBox(
    value: String,
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
    // TODO: [Low] Migrate to Layout?
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
                        val width = 1.dp.toPx()
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
                    LocalTextStyle provides UiKitTheme.typographyReworked.caption1.regular,
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
                targetValue = colors.getDescriptionColor(isEnabled),
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
    val disabledTextColor: Color,
    val disabledPlaceholderColor: Color,
    val disabledLabelColor: Color,
    val disabledLeadingContentColor: Color,
    val disabledInnerTrailingContentColor: Color,
    val disabledOuterTrailingContentColor: Color,
    val disabledDescriptionColor: Color,
    val disabledIndicationLineColor: Color,
    val disabledErrorIndicationLineColor: Color,
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

    fun getDescriptionColor(isEnabled: Boolean): Color =
        if (isEnabled) descriptionColor else disabledDescriptionColor

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

enum class ZarinaTextFieldSize { Large, Small }

object ZarinaTextFieldDefaults {
    val IconSizeLarge: Dp get() = 20.dp
    val IconSizeSmall: Dp get() = 16.dp

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ClearButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        iconSize: Dp = IconSizeLarge,
        indication: Indication? = rememberRipple(bounded = false, radius = 8.dp),
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            IconButtonCustom(
                onClick = onClick,
                indication = indication,
                modifier = modifier,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_clear_new_24),
                    contentDescription = stringResource(R.string.clear),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }

    @Composable
    fun CancelButton(
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
                style = UiKitTheme.typographyReworked.caption1.regular,
            )
        }
    }

    @Composable
    fun colors(
        textColor: Color = UiKitTheme.colorsReworked.text.general.regular.default,
        placeholderColor: Color = UiKitTheme.colorsReworked.text.general.regular.muted,
        labelColor: Color = UiKitTheme.colorsReworked.text.general.regular.muted,
        leadingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.muted,
        innerTrailingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.default,
        outerTrailingContentColor: Color = UiKitTheme.colorsReworked.text.button.outline.default, // TODO: [Low] Change to button-cell-default
        descriptionColor: Color = UiKitTheme.colorsReworked.text.general.regular.muted,
        indicationLineColor: Color = UiKitTheme.colorsReworked.border.general.default,
        activeIndicationLineColor: Color = UiKitTheme.colorsReworked.border.general.active,
        errorIndicationLineColor: Color = UiKitTheme.colorsReworked.border.general.error,
        disabledTextColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledPlaceholderColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledLabelColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledLeadingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.disabled,
        disabledInnerTrailingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.disabled,
        disabledOuterTrailingContentColor: Color = UiKitTheme.colorsReworked.text.button.outline.disabled, // TODO: [Low] Change to button-cell-disabled
        disabledDescriptionColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledIndicationLineColor: Color = UiKitTheme.colorsReworked.border.general.disabled,
        disabledErrorIndicationLineColor: Color = UiKitTheme.colorsReworked.border.general.errorDisabled,
    ): ZarinaTextFieldColors = ZarinaTextFieldColors(
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
        disabledTextColor = disabledTextColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        disabledLabelColor = disabledLabelColor,
        disabledLeadingContentColor = disabledLeadingContentColor,
        disabledInnerTrailingContentColor = disabledInnerTrailingContentColor,
        disabledOuterTrailingContentColor = disabledOuterTrailingContentColor,
        disabledDescriptionColor = disabledDescriptionColor,
        disabledIndicationLineColor = disabledIndicationLineColor,
        disabledErrorIndicationLineColor = disabledErrorIndicationLineColor,
    )

    @Composable
    fun textStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Large -> UiKitTheme.typographyReworked.primary.light
        ZarinaTextFieldSize.Small -> UiKitTheme.typographyReworked.secondary.light
    }

    @Composable
    fun labelTextStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Large -> UiKitTheme.typographyReworked.tertiary.light
        ZarinaTextFieldSize.Small -> UiKitTheme.typographyReworked.footnote.light
    }

    @Composable
    fun descriptionTextStyleFromSize(size: ZarinaTextFieldSize): TextStyle = when (size) {
        ZarinaTextFieldSize.Large -> UiKitTheme.typographyReworked.tertiary.light
        ZarinaTextFieldSize.Small -> UiKitTheme.typographyReworked.footnote.light
    }

    @Stable
    fun textVerticalPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Large -> 8.dp
        ZarinaTextFieldSize.Small -> 6.dp
    }

    @Stable
    fun labelPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Large -> 4.dp
        ZarinaTextFieldSize.Small -> 2.dp
    }

    @Stable
    fun descriptionPaddingFromSize(size: ZarinaTextFieldSize): Dp = when (size) {
        ZarinaTextFieldSize.Large -> 12.dp
        ZarinaTextFieldSize.Small -> 8.dp
    }
}

@Preview
@Composable
fun Preview() {
    ZarinaPreview {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            var text by remember { mutableStateOf("") }
            val isError by remember { derivedStateOf { text.contains("error") } }
            val isEnabled by remember { derivedStateOf { !text.contains("dis") } }

            val zarinaTextField = @Composable { size: ZarinaTextFieldSize ->
                val iconSize = when (size) {
                    ZarinaTextFieldSize.Large -> 20.dp
                    ZarinaTextFieldSize.Small -> 16.dp
                }
                ZarinaTextField(
                    value = text,
                    onValueChanged = { text = it },
                    isEnabled = isEnabled,
                    isError = isError,
                    size = size,
                    label = {
                        Text(text = "Label")
                    },
                    placeholder = {
                        Text(text = "Placeholder ${size.name.lowercase()}")
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_24),
                            contentDescription = null,
                            modifier = Modifier.size(iconSize),
                        )
                    },
                    innerTrailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(iconSize)
                                .clickable { text = "" },
                        )
                    },
                    outerTrailingContent = {
                        Text(
                            text = "Button".uppercase(),
                            color = UiKitTheme.colorsReworked.text.button.secondary.default,
                        )
                    },
                    description = {
                        Text(text = "Description")
                    },
                    modifier = Modifier.padding(16.dp),
                )
            }

            zarinaTextField(ZarinaTextFieldSize.Large)
            zarinaTextField(ZarinaTextFieldSize.Small)
        }
    }
}
