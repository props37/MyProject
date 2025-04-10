package ru.livetyping.zarina.core.uimodel.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.CourierDeliveryCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromPickupPointCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PostDeliveryCheckoutParams
import ru.livetyping.zarina.core.uimodel.cart.CartTypeParcelable
import ru.livetyping.zarina.core.uimodel.geo.AddressParcelable
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.core.uimodel.store.StoreParcelable

@Serializable
@Parcelize
public sealed class CheckoutParamsParcelable : Parcelable {
    public abstract val cartType: CartTypeParcelable
    public abstract val deliveryMethod: DeliveryMethodParcelable
    public abstract val cityKladrId: String
    public abstract val recipient: RecipientParcelable

    public abstract fun toCheckoutParams(): CheckoutParams

    public companion object {
        public fun from(params: CheckoutParams): CheckoutParamsParcelable {
            return when (params) {
                is CourierDeliveryCheckoutParams -> {
                    CourierDeliveryCheckoutParamsParcelable.from(params)
                }

                is PostDeliveryCheckoutParams -> {
                    PostDeliveryCheckoutParamsParcelable.from(params)
                }

                is PickupFromPickupPointCheckoutParams -> {
                    PickupFromPickupPointCheckoutParamsParcelable.from(params)
                }

                is PickupFromStoreCheckoutParams -> {
                    PickupFromStoreCheckoutParamsParcelable.from(params)
                }
            }
        }
    }
}

@Serializable
@Parcelize
public data class CourierDeliveryCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethod: DeliveryMethodParcelable,
    override val recipient: RecipientParcelable,
    val address: AddressParcelable,
    val deliveryOption: DeliveryOptionParcelable,
    val dateTimePeriod: DeliveryOptionParcelable.DateTimePeriodParcelable,
) : CheckoutParamsParcelable() {
    override val cityKladrId: String get() = address.city.id

    public override fun toCheckoutParams(): CourierDeliveryCheckoutParams {
        return CourierDeliveryCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethod = deliveryMethod.toDeliveryMethod(),
            recipient = recipient.toRecipient(),
            address = address.toAddress(),
            deliveryOption = deliveryOption.toDeliveryOption(),
            dateTimePeriod = dateTimePeriod.toDateTimePeriod(),
        )
    }

    public companion object {
        public fun from(params: CourierDeliveryCheckoutParams): CourierDeliveryCheckoutParamsParcelable {
            return CourierDeliveryCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethod = DeliveryMethodParcelable.from(params.deliveryMethod),
                recipient = RecipientParcelable.from(params.recipient),
                address = AddressParcelable.from(params.address),
                deliveryOption = DeliveryOptionParcelable.from(params.deliveryOption),
                dateTimePeriod = DeliveryOptionParcelable.DateTimePeriodParcelable
                    .from(params.dateTimePeriod),
            )
        }
    }
}

@Serializable
@Parcelize
public data class PostDeliveryCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethod: DeliveryMethodParcelable,
    override val recipient: RecipientParcelable,
    val address: AddressParcelable,
    val deliveryOption: DeliveryOptionParcelable,
    val dateTimePeriod: DeliveryOptionParcelable.DateTimePeriodParcelable,
) : CheckoutParamsParcelable() {
    override val cityKladrId: String get() = address.city.id

    public override fun toCheckoutParams(): PostDeliveryCheckoutParams {
        return PostDeliveryCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethod = deliveryMethod.toDeliveryMethod(),
            recipient = recipient.toRecipient(),
            address = address.toAddress(),
            deliveryOption = deliveryOption.toDeliveryOption(),
            dateTimePeriod = dateTimePeriod.toDateTimePeriod(),
        )
    }

    public companion object {
        public fun from(params: PostDeliveryCheckoutParams): PostDeliveryCheckoutParamsParcelable {
            return PostDeliveryCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethod = DeliveryMethodParcelable.from(params.deliveryMethod),
                recipient = RecipientParcelable.from(params.recipient),
                address = AddressParcelable.from(params.address),
                deliveryOption = DeliveryOptionParcelable.from(params.deliveryOption),
                dateTimePeriod = DeliveryOptionParcelable.DateTimePeriodParcelable
                    .from(params.dateTimePeriod),
            )
        }
    }
}

@Serializable
@Parcelize
public data class PickupFromPickupPointCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethod: DeliveryMethodParcelable,
    override val recipient: RecipientParcelable,
    val city: CityParcelable,
    val pickupPoint: PickupPointParcelable,
    val deliveryType: PickupPointParcelable.DeliveryTypeParcelable,
    val dateTimePeriod: PickupPointParcelable.DeliveryTypeParcelable.DateTimePeriodParcelable,
) : CheckoutParamsParcelable() {
    override val cityKladrId: String get() = city.id

    public override fun toCheckoutParams(): PickupFromPickupPointCheckoutParams {
        return PickupFromPickupPointCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethod = deliveryMethod.toDeliveryMethod(),
            recipient = recipient.toRecipient(),
            city = city.toCity(),
            pickupPoint = pickupPoint.toPickupPointDetailed(),
            deliveryType = deliveryType.toDeliveryType(),
            dateTimePeriod = dateTimePeriod.toDateTimePeriod(),
        )
    }

    public companion object {
        public fun from(
            params: PickupFromPickupPointCheckoutParams,
        ): PickupFromPickupPointCheckoutParamsParcelable {
            return PickupFromPickupPointCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethod = DeliveryMethodParcelable.from(params.deliveryMethod),
                recipient = RecipientParcelable.from(params.recipient),
                city = CityParcelable.from(params.city),
                pickupPoint = PickupPointParcelable.from(params.pickupPoint),
                deliveryType = PickupPointParcelable.DeliveryTypeParcelable.from(params.deliveryType),
                dateTimePeriod = PickupPointParcelable.DeliveryTypeParcelable.DateTimePeriodParcelable
                    .from(params.dateTimePeriod),
            )
        }
    }
}

@Serializable
@Parcelize
public data class PickupFromStoreCheckoutParamsParcelable(
    override val cartType: CartTypeParcelable,
    override val deliveryMethod: DeliveryMethodParcelable,
    override val recipient: RecipientParcelable,
    val city: CityParcelable,
    val store: StoreParcelable,
) : CheckoutParamsParcelable() {
    override val cityKladrId: String get() = city.id

    public override fun toCheckoutParams(): PickupFromStoreCheckoutParams {
        return PickupFromStoreCheckoutParams(
            cartType = cartType.toCartType(),
            deliveryMethod = deliveryMethod.toDeliveryMethod(),
            recipient = recipient.toRecipient(),
            city = city.toCity(),
            store = store.toStore(),
        )
    }

    public companion object {
        public fun from(params: PickupFromStoreCheckoutParams): PickupFromStoreCheckoutParamsParcelable {
            return PickupFromStoreCheckoutParamsParcelable(
                cartType = CartTypeParcelable.from(params.cartType),
                deliveryMethod = DeliveryMethodParcelable.from(params.deliveryMethod),
                recipient = RecipientParcelable.from(params.recipient),
                city = CityParcelable.from(params.city),
                store = StoreParcelable.from(params.store),
            )
        }
    }
}
