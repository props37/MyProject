package ru.livetyping.zarina.data.checkout.impl.remote

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

internal interface CheckoutRemoteDataSource {
    suspend fun applyGiftCertificate(
        giftCertificate: GiftCertificate,
        cartFinalPrice: Int,
        cartType: CartType,
    )

    suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType)

    fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityFiasId: FiasId,
    ): Flow<List<DeliveryMethod>>

    fun getPickupPointsFlow(cityFiasId: FiasId): Flow<List<PickupPointShort>>

    fun getPickupPointFlow(
        cityFiasId: FiasId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetailed>

    fun getPickupStoresFlow(
        cityFiasId: FiasId,
        deliveryMethodType: DeliveryMethodType,
    ): Flow<List<PickupStore>>

    fun getCourierDeliveryOptionsFlow(buildingFiasId: FiasId): Flow<List<DeliveryOption>>

    fun getPostDeliveryOptionsFlow(buildingFiasId: FiasId): Flow<List<DeliveryOption>>

    fun getCartFlow(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): Flow<Cart>

    fun getPaymentMethodsFlow(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): Flow<List<PaymentMethod>>

    suspend fun getPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?,
    ): PaymentData

    suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)? = null,
    )

    suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    )

    suspend fun createOrder(params: OrderCreationParams): OrderDetailed
}
