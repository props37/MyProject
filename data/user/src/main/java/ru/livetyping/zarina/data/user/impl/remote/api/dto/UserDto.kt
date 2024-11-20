package ru.livetyping.zarina.data.user.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.network.util.checkNotNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
internal data class UserDto(
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

    @SerialName("gender")
    val gender: GenderDto? = null,

    @SerialName("email_subscribe")
    val receiveEmails: Boolean? = null,

    @SerialName("sms_subscribe")
    val receiveSms: Boolean? = null,
) {
    fun toUser(): User {
        checkNotNull(id) { ::id }
        checkNotNull(email) { ::email }
        checkNotNull(gender) { ::gender }
        val notificationSettings = User.NotificationSettings(
            receiveSms = receiveSms ?: false,
            receiveEmails = receiveEmails ?: false,
        )
        return User(
            id = User.Id(id),
            email = Email.create(email),
            phone = phone?.let { PhoneNumber.create(it) },
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate?.let {
                LocalDate.parse(it, DateTimeFormatter.ofPattern(DATE_PATTERN))
            },
            gender = gender.toGender(),
            notificationSettings = notificationSettings,
        )
    }

    private companion object {
        const val DATE_PATTERN = "dd.MM.yyyy"
    }
}
