package ru.livetyping.zarina.core.domain.model.user

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.gender.Gender
import java.time.LocalDate

// Marked as stable on config/compose/stability_config.txt
public data class User(
    val id: Id,
    val email: Email,
    val phone: PhoneNumber?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: LocalDate?,
    val gender: Gender?,
    val notificationSettings: NotificationSettings,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)

    // Marked as stable on config/compose/stability_config.txt
    public data class NotificationSettings(
        val receiveSms: Boolean,
        val receiveEmails: Boolean,
    )

    public companion object {
        public val BIRTH_DATE_MIN_VALUE: LocalDate
            get() = LocalDate.of(1900, 1, 1)
    }
}
