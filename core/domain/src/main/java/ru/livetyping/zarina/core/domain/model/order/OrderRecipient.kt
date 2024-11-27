package ru.livetyping.zarina.core.domain.model.order

import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

public data class OrderRecipient(
    val firstName: String,
    val lastName: String?,
    val email: Email,
    val phone: PhoneNumber?,
)
