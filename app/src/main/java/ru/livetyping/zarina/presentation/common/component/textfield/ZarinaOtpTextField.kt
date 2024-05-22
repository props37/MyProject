package ru.livetyping.zarina.presentation.common.component.textfield

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.text.FontFeatureSettings
import ru.livetyping.zarina.util.library.shimmer.shimmerToggleable

@Composable
fun ZarinaOtpTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilled: ((String) -> Unit)? = null,
    length: Int = 4,
    cellMinWidth: Dp = CellMinWidth,
    cellSpacedBy: Dp = CellSpacedBy,
    areNonDigitSymbolsAllowed: Boolean = false,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    textStyle: TextStyle = TextStyleDefault,
    keyboardOptions: KeyboardOptions = remember { KeyboardOptionsDefault },
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
) {
    SideEffect {
        require(length > 0) { "length $length should be greater than zero" }
    }

    val textFieldValue by remember(value) {
        mutableStateOf(TextFieldValue(value, TextRange(value.length)))
    }

    var isTextFieldFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            var processed = newValue.text
            if (!areNonDigitSymbolsAllowed) {
                processed = processed.filter { it.isDigit() }
            }
            if (processed.length <= length) {
                onValueChanged(processed)
            }

            if (processed.length == length) {
                onFilled?.invoke(processed)
            }
        },
        enabled = isEnabled,
        readOnly = isReadOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(cellSpacedBy),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val shimmer = rememberZarinaSkeletonShimmer(
                    bounds = ShimmerBounds.Window,
                    delayMillis = 0,
                    blendMode = BlendMode.DstIn,
                    shaderColors = remember {
                        listOf(
                            Color.Unspecified.copy(alpha = 1f),
                            Color.Unspecified.copy(alpha = 0.2f),
                            Color.Unspecified.copy(alpha = 1f),
                        )
                    },
                    width = 600.dp,
                )

                repeat(length) { index ->
                    Cell(
                        char = value.getOrNull(index),
                        isFocused = isTextFieldFocused && (index == value.lastIndex + 1),
                        isLoading = isLoading,
                        isError = isError,
                        textStyle = textStyle,
                        shimmer = shimmer,
                        modifier = Modifier.widthIn(min = cellMinWidth),
                    )
                }
            }
        },
        modifier = modifier
            .background(backgroundColor)
            .onFocusChanged { isTextFieldFocused = it.isFocused },
    )
}

@Composable
private fun Cell(
    char: Char?,
    isFocused: Boolean,
    isLoading: Boolean,
    isError: Boolean,
    textStyle: TextStyle,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    val textColor by animateColorAsState(
        targetValue = if (isError) {
            UiKitTheme.colors.text.general.accent.red
        } else {
            UiKitTheme.colors.text.general.regular.default
        },
        label = "Text color",
    )

    val dividerColor by animateColorAsState(
        targetValue = when {
            isError -> UiKitTheme.colors.border.general.error
            isFocused -> UiKitTheme.colors.border.general.active
            else -> UiKitTheme.colors.border.general.default
        },
        label = "Divider color",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(IntrinsicSize.Min)
            .defaultMinSize(minWidth = CellMinWidth),
    ) {
        Text(
            text = char?.toString() ?: CharPlaceholder,
            style = textStyle,
            color = textColor,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .shimmerToggleable(shimmer = shimmer, isEnabled = isLoading),
        )

        Divider(color = dividerColor)
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        val focusManager = LocalFocusManager.current

        var value by remember { mutableStateOf("") }

        ZarinaOtpTextField(
            value = value,
            onValueChanged = { value = it },
            isLoading = value == "0000",
            isError = value == "9999",
            onFilled = { focusManager.clearFocus() },
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}

private val TextStyleDefault: TextStyle
    @Composable
    get() = UiKitTheme.typography.heading2.bold
        .copy(fontFeatureSettings = FontFeatureSettings.Mono)

@Stable
private val KeyboardOptionsDefault: KeyboardOptions
    get() = KeyboardOptions(keyboardType = KeyboardType.Number)

@Stable
private val CellMinWidth: Dp get() = 40.dp

@Stable
private val CellSpacedBy: Dp get() = 12.dp

private const val CharPlaceholder = " "
