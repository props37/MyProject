package ru.livetyping.zarina.data.old.shop.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReserveRequestBody(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("email")
    val email: String,
)
