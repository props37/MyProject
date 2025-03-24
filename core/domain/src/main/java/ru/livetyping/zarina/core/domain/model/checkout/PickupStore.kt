package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.store.Store

public data class PickupStore(
    val store: Store,
    val availableItemCount: Int,
    val availableItemIds: Set<ProductOffer.Id>,
)
