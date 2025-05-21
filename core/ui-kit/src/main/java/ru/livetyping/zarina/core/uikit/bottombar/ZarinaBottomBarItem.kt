package ru.livetyping.zarina.core.uikit.bottombar

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.text.unscalable
import ru.livetyping.zarina.core.uikit.counter.ZarinaCounter
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2

@Composable
public fun ZarinaBottomBarItem(
    title: String,
    @DrawableRes
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    counterValueProvider: (() -> Int?)? = null,
) {
    val selectedColor = UiKitTheme2.colors.mainBlack
    val unselectedColor = selectedColor.copy(alpha = 0.5f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                enabled = true,
                role = Role.Tab,
                interactionSource = null,
                indication = ripple(bounded = false, color = selectedColor),
            ),
    ) {
        val color by animateColorAsState(
            targetValue = if (isSelected) selectedColor else unselectedColor,
            label = "ZarinaBottomBarItem color",
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.widthIn(min = 40.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(iconResId),
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(25.dp),
            )

            ItemCounter(
                count = counterValueProvider?.invoke(),
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = title,
            style = UiKitTheme2.typography.caption,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ItemCounter(
    count: Int?,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
        },
        contentAlignment = Alignment.TopEnd,
        label = "ItemCounter",
        modifier = modifier,
    ) { count ->
        if (count != null && count != 0) {
            ZarinaCounter(
                value = count.toString(),
                textStyle = UiKitTheme.typography.caption2.bold.unscalable(LocalDensity.current),
            )
        }
    }
}
