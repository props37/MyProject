package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PriceFilterDto
import ru.livetyping.zarina.domain.filter.Filters

@Serializable
data class FiltersRequestDto(
    @SerialName("price")
    val price: PriceFilterDto? = null,

    @SerialName("materials")
    val materials: List<String>? = null,

    @SerialName("sizes")
    val sizes: List<String>? = null,

    @SerialName("colors")
    val colors: List<String>? = null,

    @SerialName("available_for_shipping")
    val deliveryAvailability: Boolean? = null,

    @SerialName("available_for_store_pickup")
    val storePickupAvailability: List<String>? = null,
) {
    companion object {
        fun from(filters: Filters): FiltersRequestDto? {
            return if (!filters.isEmptyIgnoringSorting) {
                val materials = filters.materials?.let { filter ->
                    if (!filter.isEmpty) filter.selectedItems.map { it.id.value } else null
                }
                val sizes = filters.sizes?.let { filter ->
                    if (!filter.isEmpty) filter.selectedItems.map { it.id.value } else null
                }
                val colors = filters.colors?.let { filter ->
                    if (!filter.isEmpty) filter.selectedItems.map { it.id.value } else null
                }
                val deliveryAvailability = filters.deliveryAvailability?.let { filter ->
                    if (filter.isEnabled) true else null
                }
                val storePickupAvailability = if (filters.storePickupAvailability?.isEnabled == true) {
                    filters.pickupStores?.let { filter ->
                        filter.selectedItems.map { it.id.value }
                    }
                } else null
                FiltersRequestDto(
                    price = filters.price?.let { PriceFilterDto.from(it) },
                    materials = materials,
                    sizes = sizes,
                    colors = colors,
                    deliveryAvailability = deliveryAvailability,
                    storePickupAvailability = storePickupAvailability,
                )
            } else {
                null
            }
        }
    }
}
