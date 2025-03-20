package ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class RecipientNavActions(
    val onCloseClicked: () -> Unit,
    val onContinueClicked: (CartType, currentCheckoutStep: Int, Recipient) -> Unit,
) : NavigationActions
