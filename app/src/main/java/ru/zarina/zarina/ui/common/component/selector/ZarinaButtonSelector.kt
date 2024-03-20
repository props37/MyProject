package ru.zarina.zarina.ui.common.component.selector

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaButtonSelector(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ZarinaButtonSelectorSize = ZarinaButtonSelectorSize.Large,
    isEnabled: Boolean = true,
    isEditable: Boolean = true,
    placeholder: (@Composable () -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = {
        ZarinaButtonSelectorDefaults.TrailingArrow(isEditable = isEditable)
    },
    textStyle: TextStyle = ZarinaButtonSelectorDefaults.textStyleFromSize(size),
    content: (@Composable () -> Unit)?,
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        label?.let { content ->
            CompositionLocalProvider(
                LocalTextStyle provides UiKitTheme.typography.footnote.light,
                LocalContentColor provides UiKitTheme.colors.text.general.regular.muted,
            ) {
                content()
            }
        }

        val minHeight = when (size) {
            ZarinaButtonSelectorSize.Large -> ZarinaButtonSelectorDefaults.ButtonHeightLarge
            ZarinaButtonSelectorSize.Medium -> ZarinaButtonSelectorDefaults.ButtonHeightMedium
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = minHeight)
                .clip(ZarinaButtonSelectorDefaults.ButtonShape)
                .clickable(
                    enabled = isEnabled && isEditable,
                    role = Role.Button,
                    onClick = onClick,
                ),
        ) {
            val contentColor = if (content != null) {
                UiKitTheme.colors.text.general.regular.default
            } else {
                UiKitTheme.colors.text.general.regular.muted
            }

            CompositionLocalProvider(
                LocalTextStyle provides textStyle,
                LocalContentColor provides contentColor,
            ) {
                content?.invoke() ?: placeholder?.invoke()
            }

            trailingContent?.let { content ->
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                content()
            }
        }

        Divider(
            color = UiKitTheme.colors.border.general.default,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

enum class ZarinaButtonSelectorSize { Large, Medium }

object ZarinaButtonSelectorDefaults {
    val ButtonHeightLarge: Dp get() = 48.dp
    val ButtonHeightMedium: Dp get() = 40.dp

    val ButtonShape: Shape get() = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)

    @Composable
    fun TrailingArrow(
        isEditable: Boolean,
        modifier: Modifier = Modifier,
    ) {
        val tint by animateColorAsState(
            targetValue = if (isEditable) {
                UiKitTheme.colors.icon.regular.default
            } else {
                UiKitTheme.colors.icon.regular.disabled
            },
            label = "TrailingArrow tint",
        )

        Icon(
            painter = painterResource(R.drawable.ic_small_arrow_up_24),
            contentDescription = stringResource(R.string.select),
            tint = tint,
            modifier = modifier
                .size(16.dp)
                .rotate(degrees = 180f),
        )
    }

    @Composable
    fun textStyleFromSize(size: ZarinaButtonSelectorSize): TextStyle = when (size) {
        ZarinaButtonSelectorSize.Large -> UiKitTheme.typography.secondary.light
        ZarinaButtonSelectorSize.Medium -> UiKitTheme.typography.tertiary.light
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        var isContentVisible by remember { mutableStateOf(false) }

        ZarinaButtonSelector(
            onClick = { isContentVisible = !isContentVisible },
            placeholder = {
                Text(text = "Placeholder")
            },
            label = {
                Text(text = "Label")
            },
            content = if (isContentVisible) {
                { Text(text = "Content") }
            } else null,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
