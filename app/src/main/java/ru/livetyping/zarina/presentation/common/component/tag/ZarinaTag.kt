package ru.livetyping.zarina.presentation.common.component.tag

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.ripple.DarkRipple
import ru.livetyping.zarina.presentation.common.ripple.LightRipple
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.defaultMinSize

@Composable
fun ZarinaTag(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    shape: Shape = ZarinaTagDefaults.Shape,
    interactionSource: MutableInteractionSource? = null,
    contentPadding: PaddingValues = ZarinaTagDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (isSelected) {
            UiKitTheme.colors.background.tag.active
        } else {
            UiKitTheme.colors.background.tag.default
        },
        label = "ZarinaTag background color",
    )

    val contentColor = animateColorAsState(
        targetValue = if (isSelected) {
            UiKitTheme.colors.text.tag.active
        } else {
            UiKitTheme.colors.text.tag.default
        },
        label = "ZarinaTag content color",
    )

    val textStyle = if (isSelected) {
        UiKitTheme.typography.secondary.regular
    } else {
        UiKitTheme.typography.secondary.light
    }

    val ripple = if (isSelected) LightRipple else DarkRipple

    CompositionLocalProvider(
        LocalContentColor provides contentColor.value,
        LocalTextStyle provides textStyle,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .defaultMinSize(ZarinaTagDefaults.MinSize)
                .clip(shape)
                .drawBehind { drawRect(backgroundColor.value) }
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple,
                    enabled = onClick != null,
                    role = Role.Button,
                    onClick = { onClick?.invoke() },
                )
                .padding(contentPadding),
            content = content,
        )
    }
}

@Composable
fun ZarinaTagSkeleton(
    modifier: Modifier = Modifier,
    shimmer: Shimmer = rememberZarinaSkeletonShimmer(),
) {
    ZarinaSkeleton(
        shimmer = shimmer,
        shape = ZarinaTagDefaults.Shape,
        modifier = modifier.size(
            width = ZarinaTagDefaults.MinSize * 2,
            height = ZarinaTagDefaults.MinSize,
        )
    )
}

object ZarinaTagDefaults {
    val MinSize: Dp get() = 36.dp

    val Shape: Shape get() = RoundedCornerShape(2.dp)

    val ContentPadding: PaddingValues get() = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
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
