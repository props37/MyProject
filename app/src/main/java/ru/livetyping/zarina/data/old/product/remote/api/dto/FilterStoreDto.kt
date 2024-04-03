package ru.livetyping.zarina.data.old.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.GeoLocation
import ru.livetyping.zarina.domain.old.Shop

@Serializable
data class FilterStoreDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("xml_id")
    val xmlId: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("schedule")
    val schedule: String? = null,
) {
    fun toDomain(): Shop? {
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(name, "name")
            && ApiContract.isNotNull(address, "address")
            && ApiContract.isNotNull(schedule, "schedule")
        ) {
            Shop(
                id = id,
                name = name,
                geoLocation = GeoLocation.DEFAULT,
                address = address,
                phone = "",
                schedule = schedule
            )
        } else {
            null
        }
    }
}
