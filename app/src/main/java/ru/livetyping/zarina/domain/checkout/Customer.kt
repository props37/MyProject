package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber

data class Customer(
    val firstName: String,
    val lastName: String,
    val phone: PhoneNumber,
    val email: Email,
)
