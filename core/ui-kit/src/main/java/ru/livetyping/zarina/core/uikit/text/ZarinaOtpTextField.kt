package ru.livetyping.zarina.core.uikit.text

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.uicompose.FontFeatureSettings
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.shimmer.shimmerToggleable
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.text.ZarinaOtpTextFieldDefaults.CharPlaceholder
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaOtpTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    onFilled: ((String) -> Unit)? = null,
    length: Int = 4,
    cellMinWidth: Dp = ZarinaOtpTextFieldDefaults.CellMinWidth,
    cellSpacedBy: Dp = ZarinaOtpTextFieldDefaults.CellSpacedBy,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    textStyle: TextStyle = ZarinaOtpTextFieldDefaults.TextStyle,
    keyboardOptions: KeyboardOptions = remember { ZarinaOtpTextFieldDefaults.KeyboardOptions },
    onKeyboardAction: KeyboardActionHandler? = null,
    backgroundColor: Color = UiKitTheme.colors.background.general.regular.default,
) {
    SideEffect {
        require(length > 0) { "length $length should be greater than zero" }
    }

    SideEffect {
        state.edit { placeCursorAtEnd() }
    }

    LaunchedEffect(state, onFilled) {
        state.textAsFlow()
            .onEach {
                if (it.length == length) {
                    onFilled?.invoke(it.toString())
                }
            }
            .launchIn(this)
    }

    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        state = state,
        enabled = isEnabled,
        readOnly = isReadOnly,
        inputTransformation = InputTransformation
            .byValue { _, proposed ->
                proposed.filter { it.isDigit() }
            }
            .maxLength(length),
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = {
            Decoration(
                textFieldState = state,
                length = length,
                cellSpacedBy = cellSpacedBy,
                cellMinWidth = cellMinWidth,
                isLoading = isLoading,
                isError = isError,
                isFocused = isFocused,
                textStyle = textStyle,
            )
        },
        modifier = modifier
            .background(backgroundColor)
            .onFocusChanged { isFocused = it.isFocused },
    )
}

@Composable
private fun Decoration(
    textFieldState: TextFieldState,
    length: Int,
    cellSpacedBy: Dp,
    cellMinWidth: Dp,
    isLoading: Boolean,
    isError: Boolean,
    isFocused: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(cellSpacedBy),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
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

        val otp = textFieldState.text

        repeat(length) { index ->
            Cell(
                char = otp.getOrNull(index),
                isFocused = isFocused && (index == otp.lastIndex + 1),
                isLoading = isLoading,
                isError = isError,
                textStyle = textStyle,
                shimmer = shimmer,
                modifier = Modifier.widthIn(min = cellMinWidth),
            )
        }
    }
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
            .defaultMinSize(minWidth = ZarinaOtpTextFieldDefaults.CellMinWidth),
    ) {
        Text(
            text = char?.toString() ?: CharPlaceholder,
            style = textStyle,
            color = textColor,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .shimmerToggleable(shimmer = shimmer, isEnabled = isLoading),
        )

        ZarinaDivider(color = dividerColor)
    }
}

public object ZarinaOtpTextFieldDefaults {
    internal val TextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.heading2.bold
            .copy(fontFeatureSettings = FontFeatureSettings.Mono)

    internal val KeyboardOptions: KeyboardOptions
        get() = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        )

    internal val CellMinWidth: Dp get() = 40.dp
    internal val CellSpacedBy: Dp get() = 12.dp

    internal const val CharPlaceholder = " "
}
