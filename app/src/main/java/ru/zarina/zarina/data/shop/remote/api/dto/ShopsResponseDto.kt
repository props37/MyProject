package ru.zarina.zarina.data.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.shop.ShopCountry

@Serializable
data class ShopsResponseDto(
    @SerialName("shops")
    val shops: List<ShopCountryDto>? = null,
) {

    fun toDomain(): List<ShopCountry> {
        return shops?.mapNotNull { it.toDomain() }.orEmpty()
    }

}
