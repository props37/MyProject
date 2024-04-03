package ru.livetyping.zarina.data.old.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.ListFilter
import ru.livetyping.zarina.domain.old.TreeFilter

@Serializable
data class FiltersDto(
    @SerialName("attributes")
    val attributes: List<AttributeFilterDto?>? = null,
    @SerialName("available_for_shipping")
    val availableForShipping: Boolean? = null,
    @SerialName("available_for_store_pickup")
    val availableForStorePickup: AvailableForStorePickupDto? = null,
    @SerialName("sizes")
    val sizes: List<SizeFilterDto?>? = null,
    @SerialName("colors")
    val colors: List<ColorFilterDto?>? = null,
    @SerialName("materials")
    val materials: List<MaterialFilterDto?>? = null,
    @SerialName("price")
    val price: PriceFilterDto? = null,
) {
    fun toDomain(categoryFilter: TreeFilter?): Filtration {
        return Filtration(
            priceLimits = price?.toDomain(),
            categories = categoryFilter,
            colors = colors?.colorsToDomain(),
            attributes = attributes?.toDomain(true),
            sizes = sizes?.toDomain(false),
            materials = materials?.toDomain(false),
            isShippingAvailable = availableForShipping,
            isPickupAvailable = availableForStorePickup?.isApplied,
            pickupShop = availableForStorePickup?.stores?.firstOrNull()?.toDomain(),
        )
    }
}

private fun List<ColorFilterDto?>.colorsToDomain(): ListFilter {
    return ListFilter(
        items = this.mapNotNull { it?.toDomain() },
        isSingleSelection = false,
    )
}

private fun List<FilterItemDto?>.toDomain(isSingleSelection: Boolean): ListFilter {
    return ListFilter(
        items = this.mapNotNull { it?.toDomain() },
        isSingleSelection = isSingleSelection,
    )
}
