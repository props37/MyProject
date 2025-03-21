package ru.livetyping.zarina.data.cart.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.network.zarina.dto.CartTypeDto

@Serializable
internal data class SetBonusRedemptionRequestBody(
    @SerialName("is_charging_off_applied")
    val isRedemptionApplied: Boolean,

    @SerialName("bonuses_to_charge_off")
    val bonusCountToRedeem: Int,

    @SerialName("cart_type")
    val cartType: CartTypeDto,
)
