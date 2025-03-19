package ru.livetyping.zarina.feature.cart.ui.impl.impl.customer

import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.Recipient

internal sealed interface RecipientScreenAction {
    data object CloseClicked : RecipientScreenAction

    data class ContinueClicked(
        val cartType: CartType,
        val step: Int,
        val recipient: Recipient,
    ) : RecipientScreenAction
}
