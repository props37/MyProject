package ru.livetyping.zarina.data.old.user.local.entity

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.AddressId
import ru.livetyping.zarina.domain.old.City

@Serializable
data class CityDataEntity(
    val id: String? = null,
    val name: String? = null,
    val region: String? = null,
) {
    fun toDomain(): City? {
        return if (
            ApiContract.isNotNull(id)
            && ApiContract.isNotNull(name)
            && ApiContract.isNotNull(region)
        )
            City(AddressId(id), name, region)
        else
            null
    }

    companion object {
        fun from(city: City): CityDataEntity {
            return CityDataEntity(city.id.id, city.name, city.region)
        }
    }
}
