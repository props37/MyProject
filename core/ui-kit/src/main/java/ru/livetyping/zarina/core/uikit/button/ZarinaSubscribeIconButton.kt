package ru.livetyping.zarina.core.uikit.button

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
public fun ZarinaSubscribeIconButton(
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
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_bell_24),
            contentDescription = stringResource(RCommon.string.res_subscribe_to_product),
            tint = UiKitTheme.colors.icon.regular.default,
            modifier = Modifier.size(iconSize),
        )
    }
}
