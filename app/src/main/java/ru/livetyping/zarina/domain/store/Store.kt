package ru.livetyping.zarina.domain.store

import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.location.Location

data class Store(
    val id: Id,
    val name: String,
    val address: String,
    val phone: PhoneNumber?,
    val schedule: String?,
    val location: Location,
    val country: String?,
    val cityKladrId: KladrId?,
    val cityName: String?,
) {
    fun getCity(): City? {
        return if (cityKladrId != null && cityName != null) {
            City(
                name = cityName,
                id = cityKladrId,
                fullName = null,
                region = null,
            )
        } else null
    }

    @JvmInline
    value class Id(val value: String)
}
