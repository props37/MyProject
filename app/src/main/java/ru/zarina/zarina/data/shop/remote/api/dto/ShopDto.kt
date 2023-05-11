package ru.zarina.zarina.data.shop.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Shop

@Serializable
data class ShopDto(
    val id: String? = null,
) {

    fun toDomain(): Shop? {
        return if (
            ApiContract.isNotNull(id, "id")
        )
            Shop(id)
        else
            null
    }

}
