package ru.livetyping.zarina.feature.cart.ui.api

import ru.livetyping.zarina.core.navigation.ScreenResult
import java.util.UUID

public data class PaymentResult(
    override val id: String = UUID.randomUUID().toString(),
) : ScreenResult
