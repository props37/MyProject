package ru.zarina.zarina.ui.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.theme.old.UiKitTheme

// TODO: [Low] Write custom checkbox
@Composable
fun ZarinaCheckbox(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: ZarinaCheckboxSize = ZarinaCheckboxSize.Large,
) {
    val sizeDp = when (size) {
        ZarinaCheckboxSize.Large -> 20.dp
        ZarinaCheckboxSize.Small -> 16.dp
    }
    val shape = RoundedCornerShape(2.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(sizeDp)
            .border(
                width = 0.5.dp,
                color = UiKitTheme.colorsReworked.background.general.inversed.default,
                shape = shape,
            )
            .toggleable(
                value = isChecked,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = sizeDp),
                enabled = true,
                role = Role.Checkbox,
                onValueChange = onCheckedChanged,
            )
            .clip(shape)
            .minimumInteractiveComponentSize(),
    ) {
        AnimatedVisibility(
            visible = isChecked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.matchParentSize(),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_checkbox_24),
                contentDescription = null,
                tint = UiKitTheme.colorsReworked.icon.regular.default,
            )
        }
    }
}

enum class ZarinaCheckboxSize { Large, Small }

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        var isChecked by remember { mutableStateOf(false) }

        ZarinaCheckbox(
            isChecked = isChecked,
            onCheckedChanged = { isChecked = it },
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        )
    }
}
