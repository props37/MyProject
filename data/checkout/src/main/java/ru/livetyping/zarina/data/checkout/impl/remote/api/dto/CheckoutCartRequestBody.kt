package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.core.network.zarina.dto.DeliveryMethodTypeDto

@Serializable
internal data class CheckoutCartRequestBody(
    @SerialName("shipping_method_type")
    val deliveryMethodType: DeliveryMethodTypeDto,

    @SerialName("address") 
    val address: Address,

    @SerialName("payload")
    val payload: Payload,
) {
    @Serializable
    data class Address(
        @SerialName("city_name")
        val cityName: String? = null,

        @SerialName("city_kladr_id")
        val cityFiasId: String? = null,

        @SerialName("street_name")
        val streetName: String? = null,

        @SerialName("street_kladr_id")
        val streetFiasId: String? = null,

        @SerialName("building_number")
        val buildingNumber: String? = null,

        @SerialName("building_kladr_id") 
        val buildingFiasId: String? = null,

        @SerialName("flat")
        val apartment: String? = null,
    )

    @Serializable
    data class Payload(
        @SerialName("shop_id")
        val shopId: String? = null,

        @SerialName("trying_type_level_name")
        val deliveryOptionId: String? = null,

        @SerialName("period_id")
        val dateTimePeriodId: String? = null,

        @SerialName("pickup_station_id")
        val pickupStoreId: String? = null,
    )

    companion object {
        fun from(checkoutParams: CheckoutParams): CheckoutCartRequestBody {
            return CheckoutCartRequestBody(
                deliveryMethodType = DeliveryMethodTypeDto.from(checkoutParams.deliveryMethod.type),
                address = getAddress(checkoutParams),
                payload = getPayload(checkoutParams)
            )
        }

        private fun getAddress(checkoutParams: CheckoutParams): Address {
            return Address(
                cityName = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.city.name
                    is PostDeliveryCheckoutParams -> checkoutParams.address.city.name
                    is PickupFromPickupPointCheckoutParams -> checkoutParams.city.name
                    is PickupFromStoreCheckoutParams -> checkoutParams.city.name
                },
                cityFiasId = checkoutParams.cityFiasId.value,
                streetName = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.street.name
                    is PostDeliveryCheckoutParams -> checkoutParams.address.street.name
                    else -> null
                },
                streetFiasId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.street.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.address.street.id.value
                    else -> null
                },
                buildingNumber = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.building.name
                    is PostDeliveryCheckoutParams -> checkoutParams.address.building.name
                    else -> null
                },
                buildingFiasId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.building.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.address.building.id.value
                    else -> null
                },
                apartment = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.apartment
                    is PostDeliveryCheckoutParams -> checkoutParams.address.apartment
                    else -> null
                },
            )
        }

        private fun getPayload(checkoutParams: CheckoutParams): Payload {
            return Payload(
                shopId = when (checkoutParams) {
                    is PickupFromStoreCheckoutParams -> checkoutParams.store.id.value
                    else -> null
                },
                deliveryOptionId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.deliveryOption.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.deliveryOption.id.value
                    is PickupFromPickupPointCheckoutParams -> checkoutParams.deliveryType.id.value
                    else -> null
                },
                dateTimePeriodId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.dateTimePeriod.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.dateTimePeriod.id.value
                    is PickupFromPickupPointCheckoutParams -> checkoutParams.dateTimePeriod.id.value
                    else -> null
                },
                pickupStoreId = when (checkoutParams) {
                    is PickupFromPickupPointCheckoutParams -> checkoutParams.pickupPoint.id.value
                    else -> null
                }
            )
        }
    }
}
