package ru.livetyping.zarina.domain.user

import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import java.time.LocalDate

data class User(
    val id: Id,
    val email: Email,
    val phone: PhoneNumber?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: LocalDate?,
    val notificationSettings: NotificationSettings,
) {
    @JvmInline
    value class Id(val value: String)

    data class NotificationSettings(
        val receiveSms: Boolean,
        val receiveEmails: Boolean,
    )
}
