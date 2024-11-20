package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import timber.log.Timber

@Serializable
public data class CityDto(
    @SerialName("name")
    val name: String? = null,

    @SerialName("kladr_id")
    val kladrId: String? = null,

    @SerialName("full_name")
    val fullName: String? = null,

    @SerialName("region")
    val region: String? = null,
) {
    public fun toCity(): City? {
        return if (name != null && kladrId != null) {
            return City(
                name = name,
                id = KladrId(kladrId),
                fullName = fullName,
                region = region,
            )
        } else {
            Timber.e("Drop City because its name or kladrId is null")
            null
        }
    }
}
