package ru.livetyping.zarina.data.user.local.entity

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId

@Serializable
data class CityEntity(
    val name: String,
    val id: String,
    val fullName: String?,
    val region: String?,
) {
    fun toCity(): City = City(
        name = name,
        id = KladrId(id),
        fullName = fullName,
        region = region,
    )

    companion object {
        fun from(city: City): CityEntity = CityEntity(
            name = city.name,
            id = city.id.value,
            fullName = city.fullName,
            region = city.region,
        )
    }
}
