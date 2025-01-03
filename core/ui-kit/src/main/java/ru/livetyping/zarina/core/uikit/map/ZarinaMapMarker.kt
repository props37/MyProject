package ru.livetyping.zarina.core.uikit.map

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
public fun ZarinaMapMarker(
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_map_store_marker_24),
        contentDescription = contentDescription,
        tint = UiKitTheme.colors.icon.regular.default,
        modifier = modifier.defaultMinSize(Size, Size),
    )
}

private val Size: Dp get() = 36.dp
