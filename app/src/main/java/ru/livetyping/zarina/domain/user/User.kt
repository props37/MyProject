package ru.livetyping.zarina.domain.user

import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.common.PhoneNumber
import java.time.LocalDate

data class User(
    val id: Id,
    val email: Email,
    val phone: PhoneNumber?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: LocalDate?,
    val gender: Gender,
    val notificationSettings: NotificationSettings,
) {
    @JvmInline
    value class Id(val value: String)

    data class NotificationSettings(
        val receiveSms: Boolean,
        val receiveEmails: Boolean,
    )

    companion object {
        val BIRTH_DATE_DEFAULT: LocalDate
            get() = LocalDate.of(1900, 1, 1)
    }
}
