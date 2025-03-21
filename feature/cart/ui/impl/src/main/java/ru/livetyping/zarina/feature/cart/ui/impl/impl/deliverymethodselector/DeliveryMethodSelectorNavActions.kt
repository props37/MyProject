package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class DeliveryMethodSelectorNavActions(
    val onBackClicked: () -> Unit,
    val onCloseClicked: () -> Unit,
    val onDeliveryMethodSelected: (CartType, currentCheckoutStep: Int, Recipient, DeliveryMethod) -> Unit,
) : NavigationActions
