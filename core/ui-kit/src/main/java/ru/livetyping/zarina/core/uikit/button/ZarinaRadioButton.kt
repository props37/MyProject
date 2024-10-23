package ru.livetyping.zarina.core.uikit.button

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaRadioButtonDefaults.SelectedBorderWidth
import ru.livetyping.zarina.core.uikit.button.ZarinaRadioButtonDefaults.Size
import ru.livetyping.zarina.core.uikit.button.ZarinaRadioButtonDefaults.UnselectedBorderWidth
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
public fun ZarinaRadioButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) SelectedBorderWidth else UnselectedBorderWidth,
        label = "border width",
    )

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(
                interactionSource = null,
                indication = ripple(bounded = false, radius = Size),
                onClick = onClick,
            )
            .size(Size)
            .background(
                color = UiKitTheme.colors.background.general.regular.default,
                shape = shape,
            )
            .border(
                width = borderWidth,
                color = UiKitTheme.colors.border.general.active,
                shape = shape,
            ),
    )
}

public object ZarinaRadioButtonDefaults {
    internal val Size: Dp get() = 16.dp
    internal val SelectedBorderWidth: Dp get() = 5.dp
    internal val UnselectedBorderWidth: Dp get() = 0.5.dp
}
