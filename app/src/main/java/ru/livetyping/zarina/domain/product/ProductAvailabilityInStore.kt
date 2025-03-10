package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.store.Store

data class ProductAvailabilityInStore(
    val store: Store,
    val amount: Amount,
) {
    enum class Amount {
        LAST_CHANCE,
        LITTLE,
        ENOUGH,
        A_LOT,
    }
}
