package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.LocationDto
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointInfo

@Serializable
data class PickupPointDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("location")
    val location: LocationDto? = null,

    @SerialName("is_trying_available")
    val isFittingAvailable: Boolean? = null,

    @SerialName("is_card_payment_available")
    val isPaymentByCardAvailable: Boolean? = null,

    @SerialName("available_payments")
    val availablePaymentMethods: List<String>? = null,
) {
    fun toPickupPoint(): PickupPoint {
        checkNotNull(id) { "id is null" }
        checkNotNull(title) { "title is null" }
        checkNotNull(address) { "address is null" }
        checkNotNull(location) { "location is null" }
        checkNotNull(availablePaymentMethods) { "availablePaymentMethods is null" }
        return PickupPointInfo(
            id = PickupPoint.Id(id),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isFittingAvailable ?: false,
            isPaymentByCardAvailable = isPaymentByCardAvailable ?: false,
            availablePaymentMethods = getAvailablePaymentMethods(availablePaymentMethods),
        )
    }

    companion object {
        fun getAvailablePaymentMethods(methods: List<String>): Set<PickupPoint.PaymentMethod> {
            return methods.mapTo(mutableSetOf()) {
                when (it) {
                    PAYMENT_METHOD_CASH -> PickupPoint.PaymentMethod.CASH
                    PAYMENT_METHOD_CARD -> PickupPoint.PaymentMethod.CARD
                    else -> throw IllegalArgumentException("Unknown payment method $it")
                }
            }
        }

        private const val PAYMENT_METHOD_CASH = "наличными"
        private const val PAYMENT_METHOD_CARD = "банковской картой"
    }
}
