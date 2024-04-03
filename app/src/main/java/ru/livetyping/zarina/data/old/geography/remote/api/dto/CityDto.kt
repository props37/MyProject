package ru.livetyping.zarina.data.old.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.AddressId
import ru.livetyping.zarina.domain.old.City

@Serializable
data class CityDto(
    @SerialName("kladr_id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("region")
    val region: String? = null,
) {

    fun toDomain(): City? {
        if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
            && ApiContract.isNotNull(region, "region")
        ) return City(AddressId(id), name, region)
        return null
    }

}
