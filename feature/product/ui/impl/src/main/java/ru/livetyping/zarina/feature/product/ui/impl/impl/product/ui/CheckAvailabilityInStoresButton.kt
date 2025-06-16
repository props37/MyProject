package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun CheckAvailabilityInStoresButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaItem(
        onClick = onClick,
        startContent = {
            Text(
                text = stringResource(RCommon.string.res_availability_in_stores).uppercase(),
                style = ExpandableBlockHeaderTextStyle,
            )
        },
        endContent = {
            Icon(
                imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(90f),
            )
        },
        backgroundColor = UiKitTheme2.colors.lightGray,
        modifier = modifier.heightIn(min = ExpandableBlockHeaderHeight),
    )
}
