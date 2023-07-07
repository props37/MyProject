package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.ListFilter

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
    fun toDomain(): Filtration {
        return Filtration(
            priceLimits = price?.toDomain(),
            colors = colors?.colorsToDomain(),
            attributes = attributes?.attributesToDomain(),
        )
    }
}

private fun List<ColorFilterDto?>.colorsToDomain(): ListFilter {
    return ListFilter(
        items = this.mapNotNull { it?.toDomain() },
        isSingleSelection = true,
    )
}

private fun List<AttributeFilterDto?>.attributesToDomain(): ListFilter {
    return ListFilter(
        items = this.mapNotNull { it?.toDomain() },
        isSingleSelection = true,
    )
}
