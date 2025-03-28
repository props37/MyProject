package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
internal data class PickupPointDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("location")
    val location: LocationDto? = null,

    @SerialName("is_trying_available")
    val isTryingAvailable: Boolean? = null,

    @SerialName("is_card_payment_available")
    val isCardPaymentAvailable: Boolean? = null,

    @SerialName("available_payments")
    val availablePaymentMethods: List<String>? = null,
) {
    fun toPickupPointShort(): PickupPointShort {
        checkPropertyNotNull(id) { ::id }
        checkPropertyNotNull(title) { ::title }
        checkPropertyNotNull(address) { ::address }
        checkPropertyNotNull(location) { ::location }
        checkPropertyNotNull(availablePaymentMethods) { ::availablePaymentMethods }
        return PickupPointShort(
            id = PickupPoint.Id(id.toString()),
            title = title,
            address = address,
            location = location.toLocation(),
            isFittingAvailable = isTryingAvailable ?: false,
            isPaymentByCardAvailable = isCardPaymentAvailable ?: false,
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
