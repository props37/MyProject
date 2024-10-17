package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutAddress
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.PaytureInPayPaymentData
import ru.livetyping.zarina.domain.checkout.PaytureWalletPaymentData
import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.order.OrderCreationParams

@Serializable
data class CreateOrderRequestBody(
    @SerialName("contact_info")
    val contactInfo: ContactInfo,

    @SerialName("shipping")
    val delivery: Delivery,

    @SerialName("payment_method")
    val paymentMethodType: PaymentMethodTypeDto,
    
    @SerialName("myCard")
    val myCard: MyCard,
    
    @SerialName("paytureWalletUid") 
    val paytureWalletPaymentId: String? = null,
    
    @SerialName("paytureInPayUid")
    val paytureInPayPaymentId: String? = null,
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
            fun from(customer: Customer): ContactInfo {
                return ContactInfo(
                    firstName = customer.firstName,
                    lastName = customer.lastName,
                    phone = customer.phone.value,
                    email = customer.email.value,
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
            val cityKladrId: String,
            
            @SerialName("street_name")
            val streetName: String?,

            @SerialName("street_kladr_id")
            val streetKladrId: String?,

            @SerialName("building_number")
            val buildingNumber: String?,

            @SerialName("building_kladr_id")
            val buildingKladrId: String?,

            @SerialName("flat")
            val flat: String?,
        ) {
            companion object {
                fun from(checkoutParams: CheckoutParams): Address {
                    return when (checkoutParams) {
                        is CourierDeliveryCheckoutParams -> from(checkoutParams.address)
                        is PostDeliveryCheckoutParams -> from(checkoutParams.address)
                        is PickupPointDeliveryCheckoutParams -> {
                            Address(
                                cityName = checkoutParams.city.name,
                                cityKladrId = checkoutParams.city.id.value,
                                streetName = null,
                                streetKladrId = null,
                                buildingNumber = null,
                                buildingKladrId = null,
                                flat = null,
                            )
                        }

                        is StorePickupCheckoutParams -> {
                            Address(
                                cityName = checkoutParams.city.name,
                                cityKladrId = checkoutParams.city.id.value,
                                streetName = null,
                                streetKladrId = null,
                                buildingNumber = null,
                                buildingKladrId = null,
                                flat = null,
                            )
                        }
                    }
                }

                private fun from(address: CheckoutAddress): Address {
                    return Address(
                        cityName = address.city.name,
                        cityKladrId = address.city.id.value,
                        streetName = address.street.name,
                        streetKladrId = address.street.id.value,
                        buildingNumber = address.building.name,
                        buildingKladrId = address.building.id.value,
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
            val pickupPointId: Long?,

            @SerialName("trying_type_level_name")
            val tryingTypeLevelName: String?,

            @SerialName("period_id")
            val periodId: Long?,
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

                        is PostDeliveryCheckoutParams -> TODO()
                        is PickupPointDeliveryCheckoutParams -> {
                            Payload(
                                pickupStoreId = null,
                                pickupPointId = checkoutParams.pickupPoint.id.value,
                                tryingTypeLevelName = checkoutParams.deliveryType.id.value,
                                periodId = checkoutParams.dateTimePeriod.id.value,
                            )
                        }

                        is StorePickupCheckoutParams -> {
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
                    deliveryMethodType = DeliveryMethodTypeDto.from(checkoutParams.deliveryMethodType),
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
            val paytureWalletPaymentId =
                (params.paymentData as? PaytureWalletPaymentData)?.data?.paymentId?.value
            val paytureInPayPaymentId =
                (params.paymentData as? PaytureInPayPaymentData)?.data?.paymentId?.value
            return CreateOrderRequestBody(
                contactInfo = ContactInfo.from(checkoutParams.customer),
                delivery = Delivery.from(checkoutParams),
                paymentMethodType = PaymentMethodTypeDto.from(params.paymentMethodType),
                myCard = MyCard.from(params.cart),
                paytureWalletPaymentId = paytureWalletPaymentId,
                paytureInPayPaymentId = paytureInPayPaymentId,
            )
        }
    }
}
