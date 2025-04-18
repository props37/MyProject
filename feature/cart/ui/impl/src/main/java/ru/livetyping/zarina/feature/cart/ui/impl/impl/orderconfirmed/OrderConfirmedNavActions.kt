package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class OrderConfirmedNavActions(
    val onReturnToHomeClicked: () -> Unit,
    val onPayClicked: (Url) -> Unit,
) : NavigationActions
