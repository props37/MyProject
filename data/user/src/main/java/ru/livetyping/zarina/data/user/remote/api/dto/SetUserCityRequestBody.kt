package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SetUserCityRequestBody(
    @SerialName("kladr_id")
    val fiasId: String,
)
