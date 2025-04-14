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
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.model.user.User
import kotlin.time.Duration

internal interface CheckoutRemoteDataSource {
    suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType)

    fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>>

    fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPointShort>>

    fun getPickupPointFlow(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetailed>

    fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType,
    ): Flow<List<PickupStore>>

    fun getCourierDeliveryOptionsFlow(buildingKladrId: KladrId): Flow<List<DeliveryOption>>

    fun getPostDeliveryOptionsFlow(buildingKladrId: KladrId): Flow<List<DeliveryOption>>

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
