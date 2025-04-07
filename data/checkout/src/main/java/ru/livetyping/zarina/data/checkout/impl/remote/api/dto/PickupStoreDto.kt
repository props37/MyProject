package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.store.Store
import timber.log.Timber

@Serializable
internal data class PickupStoreDto(
    @SerialName("shop")
    val shop: StoreDto? = null,

    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("available_items_ids")
    val availableItemIds: List<String>? = null,
) {
    fun toPickupStore(): PickupStore? {
        val store = shop?.toStore()
        return if (store != null && itemCount != null && availableItemIds != null) {
            val availableItemIds = availableItemIds
                .mapTo(mutableSetOf()) { ProductOffer.Id(it) }
                .toSet()
            PickupStore(
                store = store,
                availableItemCount = itemCount,
                availableItemIds = availableItemIds,
            )
        } else {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to PickupStore")
            null
        }
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
        fun toStore(): Store? {
            return if (id != null && name != null && address != null && lat != null && lon != null) {
                val location = Location(lat, lon)
                return Store(
                    id = Store.Id(id),
                    name = name,
                    address = address,
                    phone = phone?.let { PhoneNumber.create(it) },
                    schedule = schedule,
                    location = location,
                    country = null, // Not provided
                    city = null, // Not provided
                )
            } else {
                Timber.tag(TAG).e("Ignore $this because it can't be mapped to Store")
                null
            }
        }
    }

    private companion object {
        private const val TAG = "PickupStoreDto"
    }
}
