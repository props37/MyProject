package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import timber.log.Timber

@Serializable
internal data class ProductAvailabilityInStoreDto(
    @SerialName("shop")
    val shop: StoreDto? = null,

    @SerialName("amount")
    val amount: AmountDto? = null,
) {
    fun toProductAvailabilityInStore(): ProductAvailabilityInStore {
        checkPropertyNotNull(shop) { ::shop }
        checkPropertyNotNull(amount) { ::amount }
        return ProductAvailabilityInStore(
            store = shop.toStore(),
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
        val lat: Double? = null,

        @SerialName("lon")
        val lon: Double? = null,
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

    @Serializable
    @JvmInline
    value class AmountDto(private val value: String) {
        fun toAmount(): ProductAvailabilityInStore.Amount = when (value) {
            VALUE_LAST_CHANCE -> ProductAvailabilityInStore.Amount.LAST_CHANCE
            VALUE_LITTLE -> ProductAvailabilityInStore.Amount.LITTLE
            VALUE_ENOUGH -> ProductAvailabilityInStore.Amount.ENOUGH
            VALUE_A_LOT -> ProductAvailabilityInStore.Amount.A_LOT
            else -> {
                Timber.tag(TAG).e("Unknown amount $value")
                ProductAvailabilityInStore.Amount.A_LOT
            }
        }

        companion object {
            private const val VALUE_LAST_CHANCE = "last_chance"
            private const val VALUE_LITTLE = "little"
            private const val VALUE_ENOUGH = "enough"
            private const val VALUE_A_LOT = "a_lot"

            private const val TAG = "AmountDto"
        }
    }
}
