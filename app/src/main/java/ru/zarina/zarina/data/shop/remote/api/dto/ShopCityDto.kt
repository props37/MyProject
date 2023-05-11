package ru.zarina.zarina.data.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.shop.ShopCity

@Serializable
data class ShopCityDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("shops")
    val shops: List<ShopDto>? = null,
) {
    fun toDomain(): ShopCity? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
        )
            ShopCity(
                id = id.toString(),
                name = name,
                shops = shops?.mapNotNull { it.toDomain() }.orEmpty()
            )
        else
            null
    }
}
