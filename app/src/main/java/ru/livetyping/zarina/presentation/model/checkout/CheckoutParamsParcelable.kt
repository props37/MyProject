package ru.livetyping.zarina.presentation.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable

@Serializable
@Parcelize
sealed class CheckoutParamsParcelable : Parcelable {
    abstract val cartType: CartTypeParcelable
    abstract val deliveryMethodType: DeliveryMethodTypeParcelable
    abstract val cityKladrId: String
    abstract val customer: CustomerParcelable

    fun toCheckoutParams(): CheckoutParams {
        return when (this) {
            is CourierDeliveryCheckoutParamsParcelable -> toCourierDeliveryCheckoutParams()
            is PickupPointDeliveryCheckoutParamsParcelable -> toPickupPointDeliveryCheckoutParams()
            is PostDeliveryCheckoutParamsParcelable -> toPostDeliveryCheckoutParams()
            is StorePickupCheckoutParamsParcelable -> toStorePickupCheckoutParams()
        }
    }

    companion object {
        fun from(params: CheckoutParams): CheckoutParamsParcelable {
            return when (params) {
                is CourierDeliveryCheckoutParams -> {
                    CourierDeliveryCheckoutParamsParcelable.from(params)
                }

                is PickupPointDeliveryCheckoutParams -> {
                    PickupPointDeliveryCheckoutParamsParcelable.from(params)
                }

                is PostDeliveryCheckoutParams -> PostDeliveryCheckoutParamsParcelable.from(params)
                is StorePickupCheckoutParams -> StorePickupCheckoutParamsParcelable.from(params)
            }
        }
    }
}

@Serializable
@Parcelize
data class CourierDeliveryCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethodType: DeliveryMethodTypeParcelable,
    val address: CheckoutAddressParcelable,
    val deliveryOptionId: String,
    val dateTimePeriodId: Long,
    override val cityKladrId: String = address.city.id,
    override val customer: CustomerParcelable,
) : CheckoutParamsParcelable() {
    fun toCourierDeliveryCheckoutParams(): CourierDeliveryCheckoutParams {
        return CourierDeliveryCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethodType = deliveryMethodType.toDeliveryMethodType(),
            address = address.toCheckoutAddress(),
            deliveryOptionId = DeliveryOption.Id(deliveryOptionId),
            dateTimePeriodId = DeliveryOption.DateTimePeriod.Id(dateTimePeriodId),
            customer = customer.toCustomer(),
        )
    }

    companion object {
        fun from(params: CourierDeliveryCheckoutParams): CourierDeliveryCheckoutParamsParcelable {
            return CourierDeliveryCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethodType = DeliveryMethodTypeParcelable.from(params.deliveryMethodType),
                address = CheckoutAddressParcelable.from(params.address),
                deliveryOptionId = params.deliveryOptionId.value,
                dateTimePeriodId = params.dateTimePeriodId.value,
                customer = CustomerParcelable.from(params.customer),
            )
        }
    }
}

@Serializable
@Parcelize
data class PickupPointDeliveryCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethodType: DeliveryMethodTypeParcelable,
    val city: CityParcelable,
    val pickupPointId: Long,
    val deliveryTypeId: String,
    val dateTimePeriodId: Long,
    override val cityKladrId: String = city.id,
    override val customer: CustomerParcelable,
) : CheckoutParamsParcelable() {
    fun toPickupPointDeliveryCheckoutParams(): PickupPointDeliveryCheckoutParams {
        return PickupPointDeliveryCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethodType = deliveryMethodType.toDeliveryMethodType(),
            city = city.toCity(),
            pickupPointId = PickupPoint.Id(pickupPointId),
            deliveryTypeId = PickupPointDetails.DeliveryType.Id(deliveryTypeId),
            dateTimePeriodId = PickupPointDetails.DeliveryType.DateTimePeriod.Id(dateTimePeriodId),
            customer = customer.toCustomer(),
        )
    }

    companion object {
        fun from(params: PickupPointDeliveryCheckoutParams): PickupPointDeliveryCheckoutParamsParcelable {
            return PickupPointDeliveryCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethodType = DeliveryMethodTypeParcelable.from(params.deliveryMethodType),
                city = CityParcelable.from(params.city),
                pickupPointId = params.pickupPointId.value,
                deliveryTypeId = params.deliveryTypeId.value,
                dateTimePeriodId = params.dateTimePeriodId.value,
                customer = CustomerParcelable.from(params.customer),
            )
        }
    }
}

@Serializable
@Parcelize
data class PostDeliveryCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethodType: DeliveryMethodTypeParcelable,
    val address: CheckoutAddressParcelable,
    val deliveryOptionId: String,
    val dateTimePeriodId: Long,
    override val cityKladrId: String = address.city.id,
    override val customer: CustomerParcelable,
) : CheckoutParamsParcelable() {
    fun toPostDeliveryCheckoutParams(): PostDeliveryCheckoutParams {
        return PostDeliveryCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethodType = deliveryMethodType.toDeliveryMethodType(),
            address = address.toCheckoutAddress(),
            deliveryOptionId = DeliveryOption.Id(deliveryOptionId),
            dateTimePeriodId = DeliveryOption.DateTimePeriod.Id(dateTimePeriodId),
            customer = customer.toCustomer(),
        )
    }

    companion object {
        fun from(params: PostDeliveryCheckoutParams): PostDeliveryCheckoutParamsParcelable {
            return PostDeliveryCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethodType = DeliveryMethodTypeParcelable.from(params.deliveryMethodType),
                address = CheckoutAddressParcelable.from(params.address),
                deliveryOptionId = params.deliveryOptionId.value,
                dateTimePeriodId = params.dateTimePeriodId.value,
                customer = CustomerParcelable.from(params.customer),
            )
        }
    }
}

@Serializable
@Parcelize
data class StorePickupCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethodType: DeliveryMethodTypeParcelable,
    val city: CityParcelable,
    val storeId: String,
    override val cityKladrId: String = city.id,
    override val customer: CustomerParcelable,
) : CheckoutParamsParcelable() {
    fun toStorePickupCheckoutParams(): StorePickupCheckoutParams {
        return StorePickupCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethodType = deliveryMethodType.toDeliveryMethodType(),
            city = city.toCity(),
            storeId = Store.Id(storeId),
            customer = customer.toCustomer(),
        )
    }

    companion object {
        fun from(params: StorePickupCheckoutParams): StorePickupCheckoutParamsParcelable {
            return StorePickupCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethodType = DeliveryMethodTypeParcelable.from(params.deliveryMethodType),
                city = CityParcelable.from(params.city),
                storeId = params.storeId.value,
                customer = CustomerParcelable.from(params.customer),
            )
        }
    }
}
