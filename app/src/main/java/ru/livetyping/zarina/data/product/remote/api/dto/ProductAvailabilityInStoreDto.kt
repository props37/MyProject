package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.location.Location
import ru.livetyping.zarina.domain.product.ProductAvailabilityInStore
import ru.livetyping.zarina.domain.store.Store
import timber.log.Timber

@Serializable
data class ProductAvailabilityInStoreDto(
    @SerialName("shop")
    val store: StoreDto? = null,

    @SerialName("amount")
    val amount: AmountDto? = null,
) {
    fun toProductAvailabilityInStore(): ProductAvailabilityInStore {
        checkNotNull(store) { "store is null" }
        checkNotNull(amount) { "amount is null" }
        return ProductAvailabilityInStore(
            store = store.toStore(),
            amount = amount.toAmount(),
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
        val latitude: Double? = null,

        @SerialName("lon")
        val longitude: Double? = null,
    ) {
        fun toStore(): Store {
            checkNotNull(id) { "id is null" }
            checkNotNull(name) { "name is null" }
            checkNotNull(address) { "address is null" }
            checkNotNull(schedule) { "schedule is null" }
            checkNotNull(phone) { "phone is null" }
            checkNotNull(latitude) { "latitude is null" }
            checkNotNull(longitude) { "longitude is null" }
            val location = Location(latitude, longitude)
            return Store(
                id = Store.Id(id),
                name = name,
                address = address,
                phone = PhoneNumber.create(phone),
                schedule = schedule,
                location = location,
                country = null, // Not provided
                cityKladrId = null, // Not provided
                cityName = null, // Not provided
            )
        }
    }

    @Serializable
    @JvmInline
    value class AmountDto(private val value: String) {
        fun toAmount(): ProductAvailabilityInStore.Amount = when (value) {
            VALUE_LAST_CHANCE -> ProductAvailabilityInStore.Amount.LAST_CHANCE
            VALUE_LITTLE -> ProductAvailabilityInStore.Amount.LITTLE
            VALUE_ENOUGH -> ProductAvailabilityInStore.Amount.ENOUGH
            VALUE_A_LOT -> ProductAvailabilityInStore.Amount.A_LOT
            else -> {
                Timber.e("Unknown amount $value")
                ProductAvailabilityInStore.Amount.A_LOT
            }
        }

        companion object {
            private const val VALUE_LAST_CHANCE = "last_chance"
            private const val VALUE_LITTLE = "little"
            private const val VALUE_ENOUGH = "enough"
            private const val VALUE_A_LOT = "a_lot"
        }
    }
}
