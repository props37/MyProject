package ru.zarina.zarina.data.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.AddressId
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class CityDto(
    @SerialName("kladr_id")
    val id: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("region")
    val region: String?,
) {

    fun toDomain(): City? {
        if (
            isNotNull(id, "id")
            && isNotNull(name, "name")
            && isNotNull(region, "region")
        ) return City(AddressId(id), name, region)
        return null
    }

}
