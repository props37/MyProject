package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.domain.model.checkout.OrderCreationParams
import ru.livetyping.zarina.core.domain.model.checkout.PaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.model.user.User
import kotlin.time.Duration

public interface CheckoutRepository {
    public suspend fun applyGiftCertificate(
        giftCertificate: GiftCertificate,
        cartFinalPrice: Int,
        cartType: CartType,
    )

    public suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType)

    public fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityFiasId: FiasId,
    ): Flow<List<DeliveryMethod>>

    public fun getPickupPointsFlow(cityFiasId: FiasId): Flow<List<PickupPointShort>>

    public fun getPickupPointFlow(
        cityFiasId: FiasId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetailed>

    public fun getPickupStoresFlow(
        cityFiasId: FiasId,
        deliveryMethodType: DeliveryMethodType,
    ): Flow<List<PickupStore>>

    public fun getCourierDeliveryOptionsFlow(buildingFiasId: FiasId): Flow<List<DeliveryOption>>

    public fun getPostDeliveryOptionsFlow(buildingFiasId: FiasId): Flow<List<DeliveryOption>>

    public fun getCartFlow(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): Flow<Cart>

    public fun getPaymentMethodsFlow(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): Flow<List<PaymentMethod>>

    public suspend fun getPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?,
    ): PaymentData

    public suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)? = null,
    )

    public suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    )

    public suspend fun createOrder(params: OrderCreationParams): OrderDetailed

    public fun getCompletedPaymentsFlow(): Flow<PaymentData>

    public fun onPaymentCompleted(paymentData: PaymentData)

    public fun clear()
}
