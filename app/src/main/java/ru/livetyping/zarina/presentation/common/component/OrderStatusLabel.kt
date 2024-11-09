package ru.livetyping.zarina.presentation.common.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import ru.livetyping.zarina.domain.order.OrderStatus
import ru.livetyping.zarina.presentation.common.component.label.ZarinaLabel
import ru.livetyping.zarina.presentation.common.component.label.ZarinaLabelDefaults
import ru.livetyping.zarina.presentation.common.component.label.ZarinaLabelSize
import ru.livetyping.zarina.presentation.common.util.domain.color
import ru.livetyping.zarina.presentation.common.util.domain.nameResId

@Composable
fun OrderStatusLabel(
    status: OrderStatus,
    modifier: Modifier = Modifier,
    size: ZarinaLabelSize = ZarinaLabelSize.Large,
) {
    OrderStatusLabel(
        statusName = stringResource(status.nameResId),
        statusColor = status.color,
        size = size,
        modifier = modifier,
    )
}

@Composable
fun OrderStatusLabel(
    statusName: String,
    statusColor: Color,
    modifier: Modifier = Modifier,
    size: ZarinaLabelSize = ZarinaLabelSize.Large,
) {
    ZarinaLabel(
        size = size,
        colors = ZarinaLabelDefaults.successColors(indicatorColor = statusColor),
        modifier = modifier,
    ) {
        Text(
            text = statusName.uppercase(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
