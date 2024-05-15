package ru.livetyping.zarina.domain.store

import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.location.Location

data class Store(
    val id: Id,
    val name: String,
    val address: String,
    val phone: PhoneNumber?,
    val schedule: String?,
    val location: Location,
    val country: String,
    val cityKladrId: KladrId?,
    val cityName: String,
) {
    @JvmInline
    value class Id(val value: String)
}
