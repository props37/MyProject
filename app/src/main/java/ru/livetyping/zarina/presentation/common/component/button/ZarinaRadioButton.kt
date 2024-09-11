package ru.livetyping.zarina.presentation.common.component.button

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun ZarinaRadioButton(
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

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var selectedIndex by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.background(Color.White)) {
            ZarinaRadioButton(
                isSelected = selectedIndex == 0,
                onClick = { selectedIndex = 0 },
            )
            ZarinaRadioButton(
                isSelected = selectedIndex == 1,
                onClick = { selectedIndex = 1 },
            )
        }
    }
}

private val Size: Dp get() = 16.dp
private val SelectedBorderWidth: Dp get() = 5.dp
private val UnselectedBorderWidth: Dp get() = 0.5.dp
