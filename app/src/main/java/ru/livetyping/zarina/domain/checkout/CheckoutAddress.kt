package ru.livetyping.zarina.domain.checkout

import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.geography.Street

data class CheckoutAddress(
    val city: City,
    val street: Street,
    val building: Building,
    val apartment: String?,
)
