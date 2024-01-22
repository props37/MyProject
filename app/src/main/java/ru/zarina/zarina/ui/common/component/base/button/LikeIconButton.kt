package ru.zarina.zarina.ui.common.component.base.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.theme.UiKitTheme

@Composable
fun LikeIconButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescriptionResId: (Boolean) -> Int = { isLiked ->
        if (isLiked) R.string.remove_from_favorites else R.string.add_to_favorites
    },
    iconSize: Dp = 24.dp,
    tint: Color = UiKitTheme.colorsReworked.icon.regular.default,
    indication: Indication? = rememberRipple(bounded = false, radius = iconSize),
    isBouncingEnabled: Boolean = true,
) {
    ZarinaIconButtonBouncing(
        onClick = onClick,
        indication = indication,
        isBouncingEnabled = isBouncingEnabled,
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isLiked,
            label = "LikeIconButton",
        ) { isLiked ->
            val iconResId = if (isLiked) R.drawable.ic_heart_24 else R.drawable.ic_heart_outline_24
            Icon(
                painter = painterResource(iconResId),
                contentDescription = stringResource(contentDescriptionResId(isLiked)),
                tint = tint,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var isLiked by remember { mutableStateOf(false) }
    LikeIconButton(
        isLiked = isLiked,
        onClick = { isLiked = !isLiked },
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
    )
}
