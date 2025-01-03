package ru.livetyping.zarina.core.uimap

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun MapMyLocationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
) {
    ZarinaButton(
        onClick = onClick,
        size = ZarinaButtonSize.Medium,
        colors = ZarinaButtonDefaults.secondaryColors(),
        contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        modifier = modifier.shadow(elevation),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_location_arrow_outline_24),
            contentDescription = stringResource(R.string.uimap_show_my_location),
            modifier = Modifier.size(20.dp),
        )
    }
}
