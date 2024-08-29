package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.domain.store.Store as DomainStore

@Serializable
data class StoreDto(
    @SerialName("shop")
    val store: Store? = null,

    @SerialName("items_count")
    val availableItemCount: Int? = null,

    @SerialName("available_items_ids")
    val availableItemIds: List<String>? = null,
) {
    fun toStore(): PickupStore {
        checkNotNull(store) { "store is null" }
        checkNotNull(availableItemCount) { "availableItemCount is null" }
        checkNotNull(availableItemIds) { "availableItemIds is null" }
        val availableItemIds = availableItemIds
            .mapTo(mutableSetOf()) { ProductOffer.Id(it) }
            .toSet()
        return PickupStore(
            store = store.toStore(),
            availableItemCount = availableItemCount,
            availableItemIds = availableItemIds,
        )
    }

    @Serializable
    data class Store(
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
        val latitude: Double? = null,

        @SerialName("lon")
        val longitude: Double? = null,

        @SerialName("city")
        val cityName: String? = null,
    ) {
        fun toStore(): DomainStore {
            checkNotNull(id) { "id is null" }
            checkNotNull(name) { "name is null" }
            checkNotNull(address) { "address is null" }
            checkNotNull(schedule) { "schedule is null" }
            checkNotNull(phone) { "phone is null" }
            checkNotNull(latitude) { "latitude is null" }
            checkNotNull(longitude) { "longitude is null" }
            val location = Location(latitude, longitude)
            return DomainStore(
                id = DomainStore.Id(id),
                name = name,
                address = address,
                phone = PhoneNumber.create(phone),
                schedule = schedule,
                location = location,
                country = null, // Not provided
                cityKladrId = null, // Not provided
                cityName = cityName,
            )
        }
    }
}
