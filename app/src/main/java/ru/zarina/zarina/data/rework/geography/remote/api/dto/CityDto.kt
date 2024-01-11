package ru.zarina.zarina.data.rework.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.geography.KladrId

@Serializable
data class CityDto(
    @SerialName("name") 
    val name: String? = null,
    
    @SerialName("full_name") 
    val fullName: String? = null,
    
    @SerialName("region") 
    val region: String? = null,

    @SerialName("kladr_id")
    val kladrId: String? = null,
) {
    fun toCity(): City {
        checkNotNull(kladrId) { "kladrId is null" }
        return City(
            name = checkNotNull(name) { "name is null" },
            fullName = checkNotNull(fullName) { "fullName is null" },
            region = checkNotNull(region) { "region is null" },
            kladrId = KladrId(kladrId),
        )
    }
}
