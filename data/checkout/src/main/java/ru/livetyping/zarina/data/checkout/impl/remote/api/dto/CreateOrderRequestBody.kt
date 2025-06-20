package ru.livetyping.zarina.data.checkout.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.OrderCreationParams
import ru.livetyping.zarina.core.domain.model.checkout.PayturePaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.domain.model.checkout.SberPaymentData
import ru.livetyping.zarina.core.network.zarina.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.core.network.zarina.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.core.domain.model.geo.Address as AddressDomain

@Serializable
internal data class CreateOrderRequestBody(
    @SerialName("contact_info")
    val contactInfo: ContactInfo,

    @SerialName("shipping")
    val delivery: Delivery,

    @SerialName("payment_method")
    val paymentMethodType: PaymentMethodTypeDto,

    @SerialName("myCard")
    val myCard: MyCard,

    @SerialName("paytureWalletUid") 
    val paytureWalletPaymentId: String?,

    @SerialName("paytureInPayUid")
    val paytureInPayPaymentId: String?,

    @SerialName("sberUid")
    val sberUid: String?,

    @SerialName("sberOrderId")
    val sberOrderId: String?,
) {
    @Serializable
    data class ContactInfo(
        @SerialName("first_name") 
        val firstName: String,
        
        @SerialName("last_name")
        val lastName: String,

        @SerialName("phone") 
        val phone: String,
        
        @SerialName("email")
        val email: String,
    ) {
        companion object {
            fun from(recipient: Recipient): ContactInfo {
                return ContactInfo(
                    firstName = recipient.firstName,
                    lastName = recipient.lastName,
                    phone = recipient.phone.value,
                    email = recipient.email.value,
                )
            }
        }
    }

    @Serializable
    data class Delivery(
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
            val cityName: String,

            @SerialName("city_kladr_id") 
            val cityFiasId: String,

            @SerialName("street_name")
            val streetName: String?,

            @SerialName("street_kladr_id")
            val streetFiasId: String?,

            @SerialName("building_number")
            val buildingNumber: String?,

            @SerialName("building_kladr_id")
            val buildingFiasId: String?,

            @SerialName("flat")
            val flat: String?,
        ) {
            companion object {
                fun from(checkoutParams: CheckoutParams): Address {
                    return when (checkoutParams) {
                        is CourierDeliveryCheckoutParams -> from(checkoutParams.address)
                        is PostDeliveryCheckoutParams -> from(checkoutParams.address)
                        is PickupFromPickupPointCheckoutParams -> {
                            Address(
                                cityName = checkoutParams.city.name,
                                cityFiasId = checkoutParams.city.id.value,
                                streetName = null,
                                streetFiasId = null,
                                buildingNumber = null,
                                buildingFiasId = null,
                                flat = null,
                            )
                        }

                        is PickupFromStoreCheckoutParams -> {
                            Address(
                                cityName = checkoutParams.city.name,
                                cityFiasId = checkoutParams.city.id.value,
                                streetName = null,
                                streetFiasId = null,
                                buildingNumber = null,
                                buildingFiasId = null,
                                flat = null,
                            )
                        }
                    }
                }

                private fun from(address: AddressDomain): Address {
                    return Address(
                        cityName = address.city.name,
                        cityFiasId = address.city.id.value,
                        streetName = address.street.name,
                        streetFiasId = address.street.id.value,
                        buildingNumber = address.building.name,
                        buildingFiasId = address.building.id.value,
                        flat = address.apartment,
                    )
                }
            }
        }

        @Serializable
        data class Payload(
            @SerialName("shop_id")
            val pickupStoreId: String?,

            @SerialName("pickup_station_id") 
            val pickupPointId: String?,

            @SerialName("trying_type_level_name")
            val tryingTypeLevelName: String?,

            @SerialName("period_id")
            val periodId: String?,
        ) {
            companion object {
                fun from(checkoutParams: CheckoutParams): Payload {
                    return when (checkoutParams) {
                        is CourierDeliveryCheckoutParams -> {
                            Payload(
                                pickupStoreId = null,
                                pickupPointId = null,
                                tryingTypeLevelName = checkoutParams.deliveryOption.id.value,
                                periodId = checkoutParams.dateTimePeriod.id.value,
                            )
                        }

                        is PostDeliveryCheckoutParams -> {
                            Payload(
                                pickupStoreId = null,
                                pickupPointId = null,
                                tryingTypeLevelName = checkoutParams.deliveryOption.id.value,
                                periodId = checkoutParams.dateTimePeriod.id.value,
                            )
                        }

                        is PickupFromPickupPointCheckoutParams -> {
                            Payload(
                                pickupStoreId = null,
                                pickupPointId = checkoutParams.pickupPoint.id.value,
                                // Need to pass "economy" in case of null?
                                tryingTypeLevelName = checkoutParams.deliveryType?.id?.value,
                                periodId = checkoutParams.dateTimePeriod?.id?.value,
                            )
                        }

                        is PickupFromStoreCheckoutParams -> {
                            Payload(
                                pickupStoreId = checkoutParams.store.id.value,
                                pickupPointId = null,
                                tryingTypeLevelName = null,
                                periodId = null,
                            )
                        }
                    }
                }
            }
        }

        companion object {
            fun from(checkoutParams: CheckoutParams): Delivery {
                return Delivery(
                    deliveryMethodType = DeliveryMethodTypeDto.from(checkoutParams.deliveryMethod.type),
                    address = Address.from(checkoutParams),
                    payload = Payload.from(checkoutParams),
                )
            }
        }
    }
    
    @Serializable
    data class MyCard(
        @SerialName("isApplied")
        val isApplied: Boolean,
        
        @SerialName("productsFirstPriceSum")
        val productFirstPriceSum: Int,
    ) {
        companion object {
            fun from(cart: Cart): MyCard {
                val myCard = cart.myCard
                return if (myCard != null) {
                    MyCard(
                        isApplied = myCard.isApplied,
                        productFirstPriceSum = myCard.productsFirstPriceSum,
                    )
                } else {
                    MyCard(
                        isApplied = false,
                        productFirstPriceSum = 0,
                    )
                }
            }
        }
    }
    
    companion object {
        fun from(params: OrderCreationParams): CreateOrderRequestBody {
            val checkoutParams = params.checkoutParams

            val payturePaymentData = params.paymentData as? PayturePaymentData
            val paytureWalletPaymentId = payturePaymentData?.paymentId?.value
            val paytureInPayPaymentId = payturePaymentData?.paymentId?.value

            val sberPaymentData = params.paymentData as? SberPaymentData
            val sberUid = sberPaymentData?.sberUid?.value
            val sberOrderId = sberPaymentData?.sberOrderId?.value

            return CreateOrderRequestBody(
                contactInfo = ContactInfo.from(checkoutParams.recipient),
                delivery = Delivery.from(checkoutParams),
                paymentMethodType = PaymentMethodTypeDto.from(params.paymentMethodType),
                myCard = MyCard.from(params.cart),
                paytureWalletPaymentId = paytureWalletPaymentId,
                paytureInPayPaymentId = paytureInPayPaymentId,
                sberUid = sberUid,
                sberOrderId = sberOrderId,
            )
        }
    }
}
