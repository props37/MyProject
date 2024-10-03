package ru.livetyping.zarina.data.checkout.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.order.remote.api.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams

@Serializable
data class CheckoutCartRequestBody(
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
        val cityKladrId: String? = null,

        @SerialName("street_name")
        val streetName: String? = null,

        @SerialName("street_kladr_id")
        val streetKladrId: String? = null,

        @SerialName("building_number")
        val buildingNumber: String? = null,
        
        @SerialName("building_kladr_id") 
        val buildingKladrId: String? = null,
        
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
        val dateTimePeriodId: Long? = null,

        @SerialName("pickup_station_id")
        val pickupStoreId: Long? = null,
    )

    companion object {
        fun from(checkoutParams: CheckoutParams): CheckoutCartRequestBody {
            return CheckoutCartRequestBody(
                deliveryMethodType = DeliveryMethodTypeDto.from(checkoutParams.deliveryMethodType),
                address = getAddress(checkoutParams),
                payload = getPayload(checkoutParams)
            )
        }

        private fun getAddress(checkoutParams: CheckoutParams): Address {
            return Address(
                cityName = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.city.name
                    is PostDeliveryCheckoutParams -> checkoutParams.address.city.name
                    is PickupPointDeliveryCheckoutParams -> checkoutParams.city.name
                    is StorePickupCheckoutParams -> checkoutParams.city.name
                },
                cityKladrId = checkoutParams.cityKladrId.value,
                streetName = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.street.name
                    is PostDeliveryCheckoutParams -> checkoutParams.address.street.name
                    else -> null
                },
                streetKladrId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.street.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.address.street.id.value
                    else -> null
                },
                buildingNumber = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.address.building.name
                    is PostDeliveryCheckoutParams -> checkoutParams.address.building.name
                    else -> null
                },
                buildingKladrId = when (checkoutParams) {
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
                    is StorePickupCheckoutParams -> checkoutParams.store.id.value
                    else -> null
                },
                deliveryOptionId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.deliveryOption.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.deliveryOption.id.value
                    is PickupPointDeliveryCheckoutParams -> checkoutParams.deliveryType.id.value
                    else -> null
                },
                dateTimePeriodId = when (checkoutParams) {
                    is CourierDeliveryCheckoutParams -> checkoutParams.dateTimePeriod.id.value
                    is PostDeliveryCheckoutParams -> checkoutParams.dateTimePeriod.id.value
                    is PickupPointDeliveryCheckoutParams -> checkoutParams.dateTimePeriod.id.value
                    else -> null
                },
                pickupStoreId = when (checkoutParams) {
                    is PickupPointDeliveryCheckoutParams -> checkoutParams.pickupPoint.id.value
                    else -> null
                }
            )
        }
    }
}
