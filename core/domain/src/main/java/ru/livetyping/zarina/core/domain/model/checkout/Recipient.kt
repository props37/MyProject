package ru.livetyping.zarina.core.domain.model.checkout

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

// Marked as stable on config/compose/stability_config.txt
public data class Recipient(
    val firstName: String,
    val lastName: String,
    val phone: PhoneNumber,
    val email: Email,
) {
    public fun getFullName(): String = "$firstName $lastName"
}
