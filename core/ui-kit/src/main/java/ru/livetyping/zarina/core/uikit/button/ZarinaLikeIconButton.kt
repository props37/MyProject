package ru.livetyping.zarina.core.uikit.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.resource.R
import ru.livetyping.zarina.core.uicompose.pressBounce

@Composable
public fun ZarinaLikeIconButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @Suppress("NAME_SHADOWING")
    contentDescriptionResId: (Boolean) -> Int = { isLiked ->
        if (isLiked) R.string.res_remove_from_wishlist else R.string.res_add_to_wishlist
    },
    iconSize: Dp = ZarinaIconButtonDefaults.IconSize,
    tint: Color = ZarinaIconButtonDefaults.IconColor,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = ripple(bounded = false, radius = iconSize),
    isBouncingEnabled: Boolean = true,
) {
    ZarinaIconButton(
        onClick = onClick,
        interactionSource = interactionSource,
        indication = indication,
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isLiked,
            label = "ZarinaLikeIconButton",
            modifier = Modifier.pressBounce(interactionSource, isBouncingEnabled),
        ) { isLiked ->
            val iconResId = if (isLiked) R.drawable.ic_heart_24 else R.drawable.ic_heart_outline_24
            Icon(
                imageVector = ImageVector.vectorResource(iconResId),
                contentDescription = stringResource(contentDescriptionResId(isLiked)),
                tint = tint,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}
