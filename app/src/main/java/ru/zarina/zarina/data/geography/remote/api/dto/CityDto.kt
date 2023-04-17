package ru.zarina.zarina.data.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.AddressId
import ru.zarina.zarina.domain.City

@Serializable
data class CityDto(
    @SerialName("kladr_id")
    val id: String?,
    @SerialName("name")
    val name: String?,
) {

    fun toDomain(): City? {
        return if (id.isNullOrBlank() || name.isNullOrBlank())
            null
        else
            City(AddressId(id), name)
    }

}
