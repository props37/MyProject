package ru.zarina.zarina.data.old.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.old.City

@Serializable
data class SetCityBody(
    @SerialName("kladr_id")
    val id: String,
)

internal fun City.toSetCityDto(): SetCityBody = SetCityBody(id.id)
