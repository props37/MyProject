package ru.livetyping.zarina.data.checkout

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.checkout.local.CheckoutLocalDataSource
import ru.livetyping.zarina.data.checkout.remote.CheckoutRemoteDataSource
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.domain.checkout.PaymentData
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.PaymentMethodType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.domain.user.User
import javax.inject.Inject
import kotlin.time.Duration

class CheckoutRepository @Inject constructor(
    private val remoteDataSource: CheckoutRemoteDataSource,
    private val localDataSource: CheckoutLocalDataSource,
) {
    fun getPickupStoresFlow(cityKladrId: KladrId): Flow<List<PickupStore>> {
        return remoteDataSource.getPickupStoresFlow(cityKladrId)
    }

    fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>> {
        return remoteDataSource.getDeliveryMethodsFlow(cartType, cityKladrId)
    }

    fun getCourierDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> {
        return remoteDataSource.getCourierDeliveryOptionsFlow(buildingKladrId)
    }

    fun getPostDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> {
        return remoteDataSource.getPostDeliveryOptionsFlow(buildingKladrId)
    }

    fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPoint>> {
        return localDataSource.getPickupPointsFlow(cityKladrId)
            .onEach { cachedPickupPoints ->
                if (cachedPickupPoints == null) {
                    val pickupPoints =
                        remoteDataSource.getPickupPointsFlow(cityKladrId).firstOrNull()
                    checkNotNull(pickupPoints) { "Failed to fetch pickup points" }
                    localDataSource.setPickupPoints(cityKladrId, pickupPoints)
                }
            }
            .filterNotNull()
    }

    fun getPickupPointDetailsFlow(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetails> {
        return remoteDataSource.getPickupPointDetailsFlow(cityKladrId, pickupPointId)
    }

    fun getCartFlow(checkoutParams: CheckoutParams, paymentMethod: PaymentMethod?): Flow<Cart> {
        return remoteDataSource.getCartFlow(checkoutParams, paymentMethod)
    }

    fun getPaymentMethodsFlow(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): Flow<List<PaymentMethod>> {
        return remoteDataSource.getPaymentMethodsFlow(checkoutParams, cart)
    }

    suspend fun getPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        pickupStoreId: Store.Id?,
    ): PaymentData {
        return remoteDataSource.getPaymentData(
            cart = cart,
            paymentMethodType = paymentMethodType,
            userId = userId,
            pickupStoreId = pickupStoreId,
        )
    }

    suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
    ) {
        remoteDataSource.awaitPaymentCompleted(
            paymentData = paymentData,
            paymentMethodType = paymentMethodType,
            pollingDelay = pollingDelay,
        )
    }

    suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        remoteDataSource.updateOrderPaymentStatus(orderId, paymentMethodType)
    }

    fun clear() {
        localDataSource.clear()
    }
}
