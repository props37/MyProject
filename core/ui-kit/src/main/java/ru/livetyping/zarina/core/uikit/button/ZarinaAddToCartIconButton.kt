package ru.livetyping.zarina.core.uikit.button

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ZarinaAddToCartIconButton(
    isAdded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = ZarinaIconButtonDefaults.IconSize,
    isLoading: Boolean = false,
) {
    ZarinaIconButton(
        onClick = onClick,
        isLoading = isLoading,
        indication = ripple(bounded = false, radius = iconSize),
        modifier = modifier,
    ) {
        Crossfade(
            targetState = isAdded,
            label = "AddToCartIconButton",
        ) { isAdded ->
            val iconResId = if (isAdded) {
                RCommon.drawable.ic_shopper_checkmark_outline_24
            } else {
                RCommon.drawable.ic_shopper_outline_24
            }
            val contentDescriptionResId = if (isAdded) {
                RCommon.string.res_remove_from_cart
            } else {
                RCommon.string.res_add_to_cart
            }

            Icon(
                imageVector = ImageVector.vectorResource(iconResId),
                contentDescription = stringResource(contentDescriptionResId),
                tint = UiKitTheme.colors.icon.regular.default,
                modifier = Modifier.size(iconSize),
            )
        }
    }
}
