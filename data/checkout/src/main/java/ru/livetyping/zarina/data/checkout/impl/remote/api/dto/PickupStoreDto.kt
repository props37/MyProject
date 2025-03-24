package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class PickupStoreDto(
    @SerialName("shop")
    val shop: StoreDto? = null,

    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("available_items_ids")
    val availableItemIds: List<String>? = null,
) {
    fun toPickupStore(): PickupStore {
        checkPropertyNotNull(shop) { ::shop }
        checkPropertyNotNull(itemCount) { ::itemCount }
        checkPropertyNotNull(availableItemIds) { ::availableItemIds }
        val availableItemIds = availableItemIds
            .mapTo(mutableSetOf()) { ProductOffer.Id(it) }
            .toSet()
        return PickupStore(
            store = shop.toStore(),
            availableItemCount = itemCount,
            availableItemIds = availableItemIds,
        )
    }

    @Serializable
    data class StoreDto(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("address")
        val address: String? = null,

        @SerialName("schedule")
        val schedule: String? = null,

        @SerialName("phone")
        val phone: String? = null,

        @SerialName("lat")
        val lat: Double? = null,

        @SerialName("lon")
        val lon: Double? = null,

        @SerialName("city")
        val city: String? = null,
    ) {
        fun toStore(): Store {
            checkPropertyNotNull(id) { ::id }
            checkPropertyNotNull(name) { ::name }
            checkPropertyNotNull(address) { ::address }
            checkPropertyNotNull(schedule) { ::schedule }
            checkPropertyNotNull(phone) { ::phone }
            checkPropertyNotNull(lat) { ::lat }
            checkPropertyNotNull(lon) { ::lon }
            val location = Location(lat, lon)
            return Store(
                id = Store.Id(id),
                name = name,
                address = address,
                phone = PhoneNumber.create(phone),
                schedule = schedule,
                location = location,
                country = null, // Not provided
                city = null, // Not provided
            )
        }
    }
}