package ru.livetyping.zarina.core.uicomponent.filtration

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun FilterEndArrowIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
        contentDescription = null,
        modifier = modifier
            .size(16.dp)
            .rotate(degrees = 90f),
    )
}
