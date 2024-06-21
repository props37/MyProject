package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.GenderDto

@Serializable
data class UpdateUserInfoRequestBody(
    @SerialName("first_name")
    val firstName: String,

    @SerialName("middle_name")
    val middleName: String?,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("date_of_birth")
    val birthDate: String,

    @SerialName("email")
    val email: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("gender")
    val gender: GenderDto,

    @SerialName("old_password")
    val oldPassword: String?,

    @SerialName("new_password")
    val newPassword: String?,
)
