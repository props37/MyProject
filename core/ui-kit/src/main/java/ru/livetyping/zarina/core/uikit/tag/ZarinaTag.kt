package ru.livetyping.zarina.core.uikit.tag

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import ru.livetyping.zarina.core.uicompose.DarkRipple
import ru.livetyping.zarina.core.uicompose.LightRipple
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaTag(
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
                .defaultMinSize(ZarinaTagDefaults.MinSize, ZarinaTagDefaults.MinSize)
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
public fun ZarinaTagSkeleton(
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

public object ZarinaTagDefaults {
    internal val MinSize: Dp get() = 36.dp

    public val Shape: Shape = ZarinaButtonDefaults.Shape

    public val ContentPadding: PaddingValues get() = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
}
