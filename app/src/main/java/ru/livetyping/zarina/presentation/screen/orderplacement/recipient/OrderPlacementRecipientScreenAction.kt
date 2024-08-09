package ru.livetyping.zarina.presentation.screen.orderplacement.recipient

import ru.livetyping.zarina.domain.cart.CartType

sealed class OrderPlacementRecipientScreenAction {
    data object OrderPlacementClosed : OrderPlacementRecipientScreenAction()

    data class RecipientValidated(
        val cartType: CartType,
        val step: Int,
    ) : OrderPlacementRecipientScreenAction()
}
