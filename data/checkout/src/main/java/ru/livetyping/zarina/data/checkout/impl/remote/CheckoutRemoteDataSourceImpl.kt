package ru.livetyping.zarina.data.checkout.impl.remote

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
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
import ru.livetyping.zarina.core.domain.model.checkout.PayturePaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointDetailed
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.checkout.SberPaymentData
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.data.checkout.impl.remote.api.CheckoutApi
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryOptionsDtoType
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

internal class CheckoutRemoteDataSourceImpl @Inject constructor(
    private val api: CheckoutApi,
) : CheckoutRemoteDataSource {
    override suspend fun applyGiftCertificate(
        giftCertificate: GiftCertificate,
        cartFinalPrice: Int,
        cartType: CartType,
    ) {
        api.applyGiftCertificate(giftCertificate, cartFinalPrice, cartType)
    }

    override suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType) {
        api.withdrawGiftCertificate(paymentMethodType)
    }

    override fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>> = flow {
        val dto = api.getDeliveryMethods(cartType, cityKladrId)
        val methods = dto.mapNotNull { it.toDeliveryMethod() }
        emit(methods)
    }

    override fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPointShort>> = flow {
        val dto = api.getPickupPoints(cityKladrId)
        val pickupPoints = dto.mapNotNull { it.toPickupPointShort() }
        emit(pickupPoints)
    }

    override fun getPickupPointFlow(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetailed> = flow {
        val dto = api.getPickupPoint(cityKladrId, pickupPointId)
        emit(dto.toPickupPointDetails())
    }

    override fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType
    ): Flow<List<PickupStore>> = flow {
        val dto = api.getPickupStores(cityKladrId, deliveryMethodType)
        val stores = dto.mapNotNull { it.toPickupStore() }
        emit(stores)
    }

    override fun getCourierDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> = flow {
        val dto = api.getCourierDeliveryOptions(buildingKladrId)
        val options = dto.toDeliveryOptions(DeliveryOptionsDtoType.COURIER)
        emit(options)
    }

    override fun getPostDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> = flow {
        val dto = api.getPostDeliveryOptions(buildingKladrId)
        val options = dto.toDeliveryOptions(DeliveryOptionsDtoType.POST)
        emit(options)
    }

    override fun getCartFlow(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): Flow<Cart> = flow {
        val dto = api.getCart(checkoutParams, paymentMethod)
        emit(dto.toCart(checkoutParams.cartType))
    }

    override fun getPaymentMethodsFlow(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): Flow<List<PaymentMethod>> = flow {
        val dto = api.getPaymentMethods(checkoutParams, cart)
        val paymentMethods = dto.mapNotNull { it.toPaymentMethod() }
        check(paymentMethods.isNotEmpty()) { "PaymentMethod list is empty" }
        emit(paymentMethods)
    }

    override suspend fun getPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?
    ): PaymentData {
        return when (paymentMethodType) {
            PaymentMethodType.PAYTURE_WALLET, PaymentMethodType.PAYTURE_IN_PAY -> {
                val dto = api.getPayturePaymentData(
                    cart = cart,
                    paymentMethodType = paymentMethodType,
                    userId = userId,
                    pickupStoreId = pickupStoreId,
                )
                dto.toPayturePaymentData()
            }

            PaymentMethodType.SBER -> {
                val dto = api.getSberPaymentData(
                    cart = cart,
                    paymentMethodType = paymentMethodType,
                    userId = userId,
                    deliveryMethodType = deliveryMethodType,
                    pickupStoreId = pickupStoreId,
                )
                dto.toSberPaymentData()
            }

            else -> error("Unsupported payment method type $paymentMethodType")
        }
    }

    override suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)?
    ) {
        when (paymentMethodType) {
            PaymentMethodType.PAYTURE_WALLET, PaymentMethodType.PAYTURE_IN_PAY -> {
                check(paymentData is PayturePaymentData) { "Payment data must be ${PayturePaymentData::class.simpleName}, but was $paymentData" }
                awaitPayturePaymentCompleted(
                    paymentData = paymentData,
                    paymentMethodType = paymentMethodType,
                    pollingDelay = pollingDelay,
                    onCheck = onCheck,
                )
            }

            PaymentMethodType.SBER -> {
                check(paymentData is SberPaymentData) { "Payment data must be ${SberPaymentData::class.simpleName}, but was $paymentData" }
                awaitSberPaymentCompleted(
                    paymentData = paymentData,
                    pollingDelay = pollingDelay,
                    onCheck = onCheck,
                )
            }

            else -> Unit
        }
    }

    override suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        api.updateOrderPaymentStatus(orderId, paymentMethodType)
    }

    override suspend fun createOrder(params: OrderCreationParams): OrderDetailed {
        return api.createOrder(params).toOrder()
    }

    private suspend fun awaitPayturePaymentCompleted(
        paymentData: PayturePaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)?,
    ) {
        var errorCount = 0
        while (coroutineContext.isActive) {
            try {
                val result = api.getPayturePaymentResult(
                    paymentMethodType = paymentMethodType,
                    paymentData = paymentData,
                )
                onCheck?.invoke()
                if (result.success == true) break
            } catch (e: Exception) {
                Timber.e(e)
                errorCount++
                if (errorCount > PAYMENT_RESULT_CHECK_MAX_ERROR_COUNT) {
                    throw e
                }
            }

            delay(pollingDelay)
        }
    }

    private suspend fun awaitSberPaymentCompleted(
        paymentData: SberPaymentData,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)?,
    ) {
        var errorCount = 0
        while (coroutineContext.isActive) {
            try {
                val result = api.getSberPaymentResult(paymentData)
                onCheck?.invoke()
                if (result.isSuccess()) break
            } catch (e: Exception) {
                Timber.e(e)
                errorCount++
                if (errorCount > PAYMENT_RESULT_CHECK_MAX_ERROR_COUNT) {
                    throw e
                }
            }

            delay(pollingDelay)
        }
    }

    private companion object {
        private const val PAYMENT_RESULT_CHECK_MAX_ERROR_COUNT = 2
    }
}
