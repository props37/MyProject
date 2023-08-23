package ru.zarina.zarina.ui.common.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun FilterButton(
    @DrawableRes
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        val foregroundColor by animateColorAsState(
            targetValue = if (isEnabled) UiKitTheme.colors.primaryContentColor else UiKitTheme.colors.disabled,
            label = "foreground color"
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(foregroundColor),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = UiKitTheme.typography.circle1718,
            color = foregroundColor,
        )
    }
}
