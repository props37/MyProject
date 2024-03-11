package ru.zarina.zarina.data.user.local.entity

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.domain.rework.geography.KladrId

@Serializable
data class CityEntity(
    val name: String,
    val fullName: String,
    val region: String,
    val kladrId: String,
) {
    fun toCity(): City = City(
        name = name,
        fullName = fullName,
        region = region,
        kladrId = KladrId(kladrId),
    )

    companion object {
        fun from(city: City): CityEntity = CityEntity(
            name = city.name,
            fullName = city.fullName,
            region = city.region,
            kladrId = city.kladrId.value,
        )
    }
}
