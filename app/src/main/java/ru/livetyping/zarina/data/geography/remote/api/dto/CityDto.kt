package ru.livetyping.zarina.data.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId
import timber.log.Timber

@Serializable
data class CityDto(
    @SerialName("name") 
    val name: String? = null,

    @SerialName("id")
    val kladrId: String? = null,
    
    @SerialName("full_name") 
    val fullName: String? = null,
    
    @SerialName("region") 
    val region: String? = null,
) {
    fun toCity(): City? {
        return if (name != null && fullName != null && region != null && kladrId != null) {
            return City(
                name = name,
                id = KladrId(kladrId),
                fullName = fullName,
                region = region,
            )
        } else {
            Timber.e("Drop City because its name, fullName, region or kladrId is null")
            null
        }
    }
}
