package ru.zarina.zarina.ui.common.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.utils.compose.minInteractionSize

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .minInteractionSize()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
            )
    ) {
        Text(
            text = text.uppercase(),
            style = UiKitTheme.typography.circle1216,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
