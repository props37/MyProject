package ru.livetyping.zarina.data.cart.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BonusWriteOffRequestBody(
    @SerialName("is_charging_off_applied")
    val isWriteOffApplied: Boolean,

    @SerialName("bonuses_to_charge_off")
    val bonusCountToWriteOff: Int,

    @SerialName("cart_type")
    val deliveryType: DeliveryTypeDto,
)
