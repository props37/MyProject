package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Filtration

@Serializable
data class FiltersRequestDto(
    @SerialName("price")
    val price: PriceFilterDto?,
    @SerialName("colors")
    val color: List<String>?,
) {
    companion object {
        fun from(filtration: Filtration): FiltersRequestDto {
            return FiltersRequestDto(
                price = filtration.price?.let { PriceFilterDto.from(it) },
                color = filtration.colors?.items?.filter { it.isSelected }?.map { it.id }
            )
        }
    }
}
