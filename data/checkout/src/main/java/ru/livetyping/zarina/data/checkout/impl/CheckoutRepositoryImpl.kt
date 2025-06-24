package ru.livetyping.zarina.data.checkout.impl

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
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.data.checkout.impl.local.CheckoutLocalDataSource
import ru.livetyping.zarina.data.checkout.impl.remote.CheckoutRemoteDataSource
import javax.inject.Inject
import kotlin.time.Duration

internal class CheckoutRepositoryImpl @Inject constructor(
    private val remoteDataSource: CheckoutRemoteDataSource,
    private val localDataSource: CheckoutLocalDataSource,
) : CheckoutRepository {
    override suspend fun applyGiftCertificate(
        giftCertificate: GiftCertificate,
        cartFinalPrice: Int,
        cartType: CartType,
    ) {
        remoteDataSource.applyGiftCertificate(giftCertificate, cartFinalPrice, cartType)
    }

    override suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType) {
        remoteDataSource.withdrawGiftCertificate(paymentMethodType)
    }

    override fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityFiasId: FiasId
    ): Flow<List<DeliveryMethod>> {
        return remoteDataSource.getDeliveryMethodsFlow(cartType, cityFiasId)
    }

    override fun getPickupPointsFlow(cityFiasId: FiasId): Flow<List<PickupPointShort>> {
        return remoteDataSource.getPickupPointsFlow(cityFiasId)
    }

    override fun getPickupPointFlow(
        cityFiasId: FiasId,
        pickupPointId: PickupPoint.Id
    ): Flow<PickupPointDetailed> {
        return remoteDataSource.getPickupPointFlow(cityFiasId, pickupPointId)
    }

    override fun getPickupStoresFlow(
        cityFiasId: FiasId,
        deliveryMethodType: DeliveryMethodType
    ): Flow<List<PickupStore>> {
        return remoteDataSource.getPickupStoresFlow(cityFiasId, deliveryMethodType)
    }

    override fun getCourierDeliveryOptionsFlow(buildingFiasId: FiasId): Flow<List<DeliveryOption>> {
        return remoteDataSource.getCourierDeliveryOptionsFlow(buildingFiasId)
    }

    override fun getPostDeliveryOptionsFlow(buildingFiasId: FiasId): Flow<List<DeliveryOption>> {
        return remoteDataSource.getPostDeliveryOptionsFlow(buildingFiasId)
    }

    override fun getCartFlow(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): Flow<Cart> {
        return remoteDataSource.getCartFlow(checkoutParams, paymentMethod)
    }

    override fun getPaymentMethodsFlow(
        checkoutParams: CheckoutParams,
        cart: Cart
    ): Flow<List<PaymentMethod>> {
        return remoteDataSource.getPaymentMethodsFlow(checkoutParams, cart)
    }

    override suspend fun getPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?,
    ): PaymentData {
        return remoteDataSource.getPaymentData(
            cart = cart,
            paymentMethodType = paymentMethodType,
            userId = userId,
            deliveryMethodType = deliveryMethodType,
            pickupStoreId = pickupStoreId,
        )
    }

    override suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)?,
    ) {
        remoteDataSource.awaitPaymentCompleted(
            paymentData = paymentData,
            paymentMethodType = paymentMethodType,
            pollingDelay = pollingDelay,
            onCheck = onCheck,
        )
    }

    override suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        remoteDataSource.updateOrderPaymentStatus(orderId, paymentMethodType)
    }

    override suspend fun createOrder(params: OrderCreationParams): OrderDetailed {
        return remoteDataSource.createOrder(params)
    }

    override fun getCompletedPaymentsFlow(): Flow<PaymentData> {
        return localDataSource.getCompletedPaymentsFlow()
    }

    override fun onPaymentCompleted(paymentData: PaymentData) {
        localDataSource.onPaymentCompleted(paymentData)
    }

    override fun clear() {
        localDataSource.clear()
    }
}
