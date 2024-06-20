package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.util.DATE_BACKEND_PATTERN
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
data class UserDto(
    @SerialName("first_name")
    val firstName: String? = null,

    @SerialName("middle_name")
    val middleName: String? = null,

    @SerialName("last_name")
    val lastName: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("date_of_birth")
    val birthDate: String? = null,

    @SerialName("gender")
    val gender: String? = null,
) {
    fun toUser(): User {
        checkNotNull(email) { "email is null" }
        checkNotNull(phone) { "phone is null" }
        return User(
            id = User.Id(UUID.randomUUID().toString()), // TODO: [Backend] Implement
            email = Email.create(email),
            phone = PhoneNumber.create(phone),
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate?.let {
                LocalDate.parse(it, DateTimeFormatter.ofPattern(DATE_BACKEND_PATTERN))
            },
            notificationSettings = User.NotificationSettings(false, false),
        )
    }
}
