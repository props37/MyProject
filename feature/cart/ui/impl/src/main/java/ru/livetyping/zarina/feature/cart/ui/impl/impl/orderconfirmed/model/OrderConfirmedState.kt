package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed

@Immutable
internal data class OrderConfirmedState(
    val order: OrderDetailed,
    val descriptionType: DescriptionType,
    val buttonType: ButtonType,
)
