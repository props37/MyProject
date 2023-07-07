package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.ListFilter

@Serializable
data class FiltersRequestDto(
    @SerialName("price")
    val price: PriceFilterDto?,
    @SerialName("colors")
    val color: List<String>?,
    @SerialName("attributes")
    val attributes: List<String>?,
    @SerialName("sizes")
    val sizes: List<String>?,
    @SerialName("materials")
    val materials: List<String>?,
) {
    companion object {
        fun from(filtration: Filtration): FiltersRequestDto {
            return FiltersRequestDto(
                price = filtration.price?.let { PriceFilterDto.from(it) },
                color = filtration.colors?.toDto(),
                attributes = filtration.attributes?.toDto(),
                materials = filtration.materials?.toDto(),
                sizes = filtration.sizes?.toDto(),
            )
        }

        private fun ListFilter.toDto(): List<String> =
            this.items.filter { it.isSelected }.map { it.id }
    }
}
