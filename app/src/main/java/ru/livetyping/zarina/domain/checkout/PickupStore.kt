package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.domain.store.Store

data class PickupStore(
    val store: Store,
    val availableItemCount: Int,
    val availableItemIds: Set<ProductOffer.Id>,
)
