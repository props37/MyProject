package ru.zarina.zarina.data.rework.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetUserCityRequestBody(
    @SerialName("kladr_id")
    val kladrId: String,
)
