package ru.livetyping.zarina.ui.common.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ru.livetyping.zarina.domain.order.OrderStatus
import ru.livetyping.zarina.ui.common.component.label.ZarinaLabel
import ru.livetyping.zarina.ui.common.component.label.ZarinaLabelDefaults
import ru.livetyping.zarina.ui.common.component.label.ZarinaLabelSize
import ru.livetyping.zarina.ui.common.util.domain.color
import ru.livetyping.zarina.ui.common.util.domain.nameResId

@Composable
fun OrderStatusLabel(
    status: OrderStatus,
    modifier: Modifier = Modifier,
    size: ZarinaLabelSize = ZarinaLabelSize.Large,
) {
    ZarinaLabel(
        size = size,
        colors = ZarinaLabelDefaults.successColors(indicatorColor = status.color),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(status.nameResId).uppercase(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
