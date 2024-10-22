package ru.livetyping.zarina.core.uikit.button

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.resource.R

@Composable
public fun ZarinaBackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.back),
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    iconSize: Dp = ZarinaIconButtonDefaults.IconSize,
    tint: Color = ZarinaIconButtonDefaults.IconColor,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = ripple(bounded = false, radius = iconSize),
) {
    ZarinaIconButton(
        onClick = onClick,
        isEnabled = isEnabled,
        isLoading = isLoading,
        loaderSize = iconSize,
        loaderColor = tint,
        interactionSource = interactionSource,
        indication = indication,
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier
                .size(iconSize)
                .rotate(degrees = 270f),
        )
    }
}
