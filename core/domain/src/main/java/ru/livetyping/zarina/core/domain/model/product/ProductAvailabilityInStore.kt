package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.store.Store

// Marked as stable on config/compose/stability_config.txt
public data class ProductAvailabilityInStore(
    val store: Store,
    val amount: Amount,
) {
    // Marked as stable on config/compose/stability_config.txt
    public enum class Amount {
        LAST_CHANCE,
        LITTLE,
        ENOUGH,
        A_LOT,
    }
}
