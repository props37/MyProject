package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateUserInfoRequestBody(
    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("date_of_birth")
    val birthDate: String,

    @SerialName("email")
    val email: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("gender")
    val gender: GenderDto?,

    @SerialName("old_password")
    val oldPassword: String?,

    @SerialName("new_password")
    val newPassword: String?,
)
