package ru.livetyping.zarina.core.uikit.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaButtonSelector(
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
    contentPadding: PaddingValues = ZarinaButtonSelectorDefaults.ContentPadding,
    applyContentPaddingToDivider: Boolean = false,
    content: (@Composable () -> Unit)?,
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        val minHeight = when (size) {
            ZarinaButtonSelectorSize.Large -> ZarinaButtonSelectorDefaults.ButtonHeightLarge
            ZarinaButtonSelectorSize.Medium -> ZarinaButtonSelectorDefaults.ButtonHeightMedium
        }

        Column(
            modifier = Modifier
                .heightIn(min = minHeight)
                .clip(ZarinaButtonSelectorDefaults.Shape)
                .clickable(
                    enabled = isEnabled && isEditable,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(contentPadding),
        ) {
            label?.let { content ->
                CompositionLocalProvider(
                    LocalTextStyle provides UiKitTheme.typography.footnote.light,
                    LocalContentColor provides UiKitTheme.colors.text.general.regular.muted,
                ) {
                    content()
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
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
        }

        val contentPaddingModifier = if (applyContentPaddingToDivider) {
            val ld = LocalLayoutDirection.current
            Modifier.padding(
                start = contentPadding.calculateStartPadding(ld),
                end = contentPadding.calculateEndPadding(ld),
            )
        } else {
            Modifier
        }

        ZarinaDivider(
            modifier = Modifier
                .fillMaxWidth()
                .then(contentPaddingModifier),
        )
    }
}

public enum class ZarinaButtonSelectorSize { Large, Medium }

public object ZarinaButtonSelectorDefaults {
    internal val ButtonHeightLarge: Dp get() = 48.dp
    internal val ButtonHeightMedium: Dp get() = 40.dp

    internal val Shape: Shape get() = ZarinaButtonDefaults.Shape

    public val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = 8.dp, vertical = 6.dp)

    @Composable
    public fun TrailingArrow(
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
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
            contentDescription = stringResource(RCommon.string.res_select),
            tint = tint,
            modifier = modifier
                .size(16.dp)
                .rotate(degrees = 180f),
        )
    }

    @Composable
    public fun textStyleFromSize(size: ZarinaButtonSelectorSize): TextStyle = when (size) {
        ZarinaButtonSelectorSize.Large -> UiKitTheme.typography.secondary.light
        ZarinaButtonSelectorSize.Medium -> UiKitTheme.typography.tertiary.light
    }
}
