package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.common.rippletheme.DarkRippleTheme
import ru.zarina.zarina.ui.common.rippletheme.LightRippleTheme
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun ZarinaTag(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    shape: Shape = ZarinaTagDefaults.Shape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentPadding: PaddingValues = ZarinaTagDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (isSelected) {
            UiKitTheme.colorsReworked.background.tag.active
        } else {
            UiKitTheme.colorsReworked.background.tag.default
        },
        label = "ZarinaTag background color",
    )

    val contentColor = animateColorAsState(
        targetValue = if (isSelected) {
            UiKitTheme.colorsReworked.text.tag.active
        } else {
            UiKitTheme.colorsReworked.text.tag.default
        },
        label = "ZarinaTag content color",
    )

    val textStyle = if (isSelected) {
        UiKitTheme.typographyReworked.secondary.regular
    } else {
        UiKitTheme.typographyReworked.secondary.light
    }

    val rippleTheme = if (isSelected) LightRippleTheme else DarkRippleTheme

    CompositionLocalProvider(
        LocalContentColor provides contentColor.value,
        LocalTextStyle provides textStyle,
        LocalRippleTheme provides rippleTheme,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .defaultMinSize(
                    minWidth = ZarinaTagDefaults.MinSize,
                    minHeight = ZarinaTagDefaults.MinSize,
                )
                .clip(shape)
                .drawBehind { drawRect(backgroundColor.value) }
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = onClick != null,
                    role = Role.Button,
                    onClick = { onClick?.invoke() },
                )
                .padding(contentPadding),
            content = content,
        )
    }
}

object ZarinaTagDefaults {
    val MinSize: Dp get() = 40.dp

    val Shape: Shape get() = RoundedCornerShape(2.dp)

    val ContentPadding: PaddingValues get() = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var isSelected by remember { mutableStateOf(false) }
        ZarinaTag(
            onClick = { isSelected = !isSelected },
            isSelected = isSelected,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            Text(text = "Одежда")
        }
    }
}
