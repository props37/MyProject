package ru.zarina.zarina.ui.common.component.textfield

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
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme

// TODO: [High] Add colors
// TODO: [High] Migrate to BasicTextField2

@Composable
fun ZarinaTextField(
    textFieldValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    readOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Large,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
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
    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChanged,
        modifier = modifier,
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
                textStyle = textStyle,
                size = size,
                innerTextField = innerTextField,
                label = label,
                placeholder = placeholder,
                leadingContent = leadingContent,
                innerTrailingContent = innerTrailingContent,
                outerTrailingContent = outerTrailingContent,
                description = description,
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
    readOnly: Boolean = false,
    size: ZarinaTextFieldSize = ZarinaTextFieldSize.Large,
    textStyle: TextStyle = ZarinaTextFieldDefaults.textStyleFromSize(size),
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    innerTrailingContent: (@Composable () -> Unit)? = null,
    outerTrailingContent: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
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
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
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
                textStyle = textStyle,
                size = size,
                innerTextField = innerTextField,
                label = label,
                placeholder = placeholder,
                leadingContent = leadingContent,
                innerTrailingContent = innerTrailingContent,
                outerTrailingContent = outerTrailingContent,
                description = description,
            )
        },
    )
}

@Composable
private fun DecorationBox(
    value: String,
    textStyle: TextStyle,
    size: ZarinaTextFieldSize,
    innerTextField: @Composable () -> Unit,
    label: (@Composable () -> Unit)?,
    placeholder: (@Composable () -> Unit)?,
    leadingContent: (@Composable () -> Unit)?,
    innerTrailingContent: (@Composable () -> Unit)?,
    outerTrailingContent: (@Composable () -> Unit)?,
    description: (@Composable () -> Unit)?,
) {
    // TODO: [High] Migrate to Layout?
    Column {
        label?.let { label ->
            val labelTextStyle = ZarinaTextFieldDefaults.labelTextStyleFromSize(size)
            val padding = ZarinaTextFieldDefaults.labelPaddingFromSize(size)
            CompositionLocalProvider(LocalTextStyle provides labelTextStyle) {
                label()
            }
            Spacer(modifier = Modifier.height(padding))
        }

        val indicationLineColor = UiKitTheme.colorsReworked.border.general.default

        Row(verticalAlignment = Alignment.CenterVertically) {
            val verticalPadding = ZarinaTextFieldDefaults.textVerticalPaddingFromSize(size)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        val width = 1.dp.toPx()
                        drawLine(
                            color = indicationLineColor,
                            start = Offset(0f, this.size.height - width),
                            end = Offset(this.size.width, this.size.height - width),
                            strokeWidth = width,
                        )
                    }
                    .padding(vertical = verticalPadding),
            ) {
                leadingContent?.let { content ->
                    content()
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Box {
                    innerTextField()
                    if (value.isEmpty() && placeholder != null) {
                        CompositionLocalProvider(LocalTextStyle provides textStyle) {
                            placeholder()
                        }
                    }
                }

                innerTrailingContent?.let { content ->
                    Spacer(modifier = Modifier.width(8.dp))
                    content()
                }
            }

            outerTrailingContent?.let { content ->
                Spacer(modifier = Modifier.width(8.dp))
                CompositionLocalProvider(
                    LocalTextStyle provides UiKitTheme.typographyReworked.caption1.regular,
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
            CompositionLocalProvider(LocalTextStyle provides descriptionTextStyle) {
                description()
            }
        }
    }
}

@Immutable
data class ZarinaTextFieldColors(
    val textColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val leadingContentColor: Color,
    val innerTrailingContentColor: Color,
    val outerTrailingContentColor: Color,
    val descriptionColor: Color,
    val indicationLineColor: Color,
    val disabledTextColor: Color,
    val disabledPlaceholderColor: Color,
    val disabledLabelColor: Color,
    val disabledLeadingContentColor: Color,
    val disabledInnerTrailingContentColor: Color,
    val disabledOuterTrailingContentColor: Color,
    val disabledDescriptionColor: Color,
    val disabledIndicationLineColor: Color,
)

enum class ZarinaTextFieldSize { Large, Small }

object ZarinaTextFieldDefaults {
    @Composable
    fun colors(
        textColor: Color = UiKitTheme.colorsReworked.text.general.regular.default,
        placeholderColor: Color = UiKitTheme.colorsReworked.text.general.regular.muted,
        labelColor: Color = UiKitTheme.colorsReworked.text.general.regular.muted,
        leadingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.muted,
        innerTrailingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.default,
        outerTrailingContentColor: Color = UiKitTheme.colorsReworked.text.button.outline.default, // TODO: [High] Change to button-cell-default
        descriptionColor: Color = UiKitTheme.colorsReworked.text.general.regular.muted,
        indicationLineColor: Color = UiKitTheme.colorsReworked.border.general.default,
        disabledTextColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledPlaceholderColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledLabelColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledLeadingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.disabled,
        disabledInnerTrailingContentColor: Color = UiKitTheme.colorsReworked.icon.regular.disabled,
        disabledOuterTrailingContentColor: Color = UiKitTheme.colorsReworked.text.button.outline.disabled, // TODO: [High] Change to button-cell-disabled
        disabledDescriptionColor: Color = UiKitTheme.colorsReworked.text.general.regular.disabled,
        disabledIndicationLineColor: Color = UiKitTheme.colorsReworked.border.general.disabled,
    ): ZarinaTextFieldColors = ZarinaTextFieldColors(
        textColor = textColor,
        placeholderColor = placeholderColor,
        labelColor = labelColor,
        leadingContentColor = leadingContentColor,
        innerTrailingContentColor = innerTrailingContentColor,
        outerTrailingContentColor = outerTrailingContentColor,
        descriptionColor = descriptionColor,
        indicationLineColor = indicationLineColor,
        disabledTextColor = disabledTextColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        disabledLabelColor = disabledLabelColor,
        disabledLeadingContentColor = disabledLeadingContentColor,
        disabledInnerTrailingContentColor = disabledInnerTrailingContentColor,
        disabledOuterTrailingContentColor = disabledOuterTrailingContentColor,
        disabledDescriptionColor = disabledDescriptionColor,
        disabledIndicationLineColor = disabledIndicationLineColor,
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
    ZarinaTheme {
        Column {
            var text by remember { mutableStateOf("") }

            val zarinaTextField = @Composable { size: ZarinaTextFieldSize ->
                val iconSize = when (size) {
                    ZarinaTextFieldSize.Large -> 20.dp
                    ZarinaTextFieldSize.Small -> 16.dp
                }
                ZarinaTextField(
                    value = text,
                    onValueChanged = { text = it },
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
                            modifier = Modifier.size(iconSize),
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
