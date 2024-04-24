package ru.livetyping.zarina.domain.order

import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber

data class OrderContactInfo(
    val firstName: String,
    val lastName: String?,
    val email: Email,
    val phone: PhoneNumber?,
)
