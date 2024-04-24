package ru.livetyping.zarina.domain.shop

import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.geography.KladrId

data class Shop(
    val id: Id,
    val name: String,
    val address: String,
    val phone: PhoneNumber?,
    val schedule: String?,
    val cityKladrId: KladrId,
    val cityName: String,
) {
    @JvmInline
    value class Id(val value: String)
}
