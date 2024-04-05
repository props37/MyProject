package ru.livetyping.zarina.data.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.dto.util.DATE_BACKEND_PATTERN
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.user.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class UserDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("name")
    val firstName: String? = null,

    @SerialName("last_name")
    val lastName: String? = null,

    @SerialName("birthday")
    val birthDate: String? = null,
) {
    fun toUser(): User {
        checkNotNull(id) { "id is null" }
        checkNotNull(email) { "email is null" }
        return User(
            id = User.Id(id),
            email = Email.create(email),
            phone = phone?.let { PhoneNumber.create(it) },
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate?.let {
                LocalDate.parse(it, DateTimeFormatter.ofPattern(DATE_BACKEND_PATTERN))
            },
        )
    }
}
