package ru.livetyping.zarina.data.old.shop.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.GeoLocation
import ru.livetyping.zarina.domain.old.Shop

@Serializable
data class ShopDto(
    val id: String? = null,
    val name: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val address: String? = null,
    val phone: String? = null,
    val schedule: String? = null,
) {

    fun toDomain(): Shop? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
            && ApiContract.isNotNull(lat, "lat")
            && ApiContract.isNotNull(lon, "lon")
            && ApiContract.isNotNull(address, "address")
            && ApiContract.isNotNull(phone, "phone")
            && ApiContract.isNotNull(schedule, "schedule")
        )
            Shop(
                id = id,
                name = name,
                geoLocation = GeoLocation(latitude = lat, longitude = lon),
                address = address,
                phone = phone,
                schedule = schedule
            )
        else
            null
    }

}
