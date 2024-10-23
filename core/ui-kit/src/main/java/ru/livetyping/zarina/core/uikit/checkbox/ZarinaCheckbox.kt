package ru.livetyping.zarina.core.uikit.checkbox

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaCheckbox(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    size: ZarinaCheckboxSize = ZarinaCheckboxSize.Large,
) {
    val sizeDp = when (size) {
        ZarinaCheckboxSize.Large -> 20.dp
        ZarinaCheckboxSize.Small -> 16.dp
    }
    val borderColor by animateColorAsState(
        targetValue = if (!isError || isChecked) {
            UiKitTheme.colors.border.general.active
        } else {
            UiKitTheme.colors.border.general.error
        },
        label = "ZarinaCheckbox border color",
    )
    val shape = RoundedCornerShape(2.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(sizeDp)
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = shape,
            )
            .toggleable(
                value = isChecked,
                interactionSource = null,
                indication = ripple(bounded = false, radius = sizeDp),
                enabled = true,
                role = Role.Checkbox,
                onValueChange = onCheckedChanged,
            )
            // Workaround as minimumInteractiveComponentSize() that placed before clickable()
            // doesn't always works as intended
            .minimumInteractiveComponentSize()
            .clip(shape),
    ) {
        AnimatedVisibility(
            visible = isChecked,
            enter = remember { fadeIn() },
            exit = remember { fadeOut() },
            modifier = Modifier.matchParentSize(),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_checkbox_24),
                contentDescription = null,
                tint = UiKitTheme.colors.icon.regular.default,
            )
        }
    }
}

public enum class ZarinaCheckboxSize { Large, Small }
