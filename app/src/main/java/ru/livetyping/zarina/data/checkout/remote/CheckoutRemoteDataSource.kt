package ru.livetyping.zarina.data.checkout.remote

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import ru.livetyping.zarina.data.checkout.remote.api.CheckoutApi
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryOptionsDtoType
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.checkout.DeliveryOption
import ru.livetyping.zarina.domain.checkout.PaymentData
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PayturePaymentData
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.checkout.SberPaymentData
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.giftcert.GiftCertificate
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.PaymentMethodType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.domain.user.User
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

class CheckoutRemoteDataSource @Inject constructor(
    private val api: CheckoutApi,
) {
    fun getPickupStoresFlow(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType,
    ): Flow<List<PickupStore>> = flow {
        val dto = api.getPickupStores(cityKladrId, deliveryMethodType)
        val stores = dto.map { it.toStore() }
        emit(stores)
    }

    fun getDeliveryMethodsFlow(
        cartType: CartType,
        cityKladrId: KladrId,
    ): Flow<List<DeliveryMethod>> = flow {
        val dto = api.getDeliveryMethods(cartType, cityKladrId)
        val methods = dto.mapNotNull { it.toDeliveryMethod() }
        emit(methods)
    }

    fun getCourierDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> = flow {
        val dto = api.getCourierDeliveryOptions(buildingKladrId)
        val options = dto.toDeliveryOptions(DeliveryOptionsDtoType.COURIER)
        emit(options)
    }

    fun getPostDeliveryOptionsFlow(
        buildingKladrId: KladrId,
    ): Flow<List<DeliveryOption>> = flow {
        val dto = api.getPostDeliveryOptions(buildingKladrId)
        val options = dto.toDeliveryOptions(DeliveryOptionsDtoType.POST)
        emit(options)
    }

    fun getPickupPointsFlow(cityKladrId: KladrId): Flow<List<PickupPoint>> = flow {
        val dto = api.getPickupPoints(cityKladrId)
        val pickupPoints = dto.map { it.toPickupPoint() }
        emit(pickupPoints)
    }

    fun getPickupPointDetailsFlow(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): Flow<PickupPointDetails> = flow {
        val dto = api.getPickupPointDetails(cityKladrId, pickupPointId)
        emit(dto.toPickupPointDetails())
    }

    fun getCartFlow(checkoutParams: CheckoutParams, paymentMethod: PaymentMethod?) = flow {
        val dto = api.getCart(checkoutParams, paymentMethod)
        emit(dto.toCart(checkoutParams.cartType))
    }

    fun getPaymentMethodsFlow(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): Flow<List<PaymentMethod>> = flow {
        val dtos = api.getPaymentMethods(checkoutParams, cart)
        val paymentMethods = dtos.mapNotNull { it.toPaymentMethod() }
        check(paymentMethods.isNotEmpty()) { "PaymentMethod list is empty" }
        emit(paymentMethods)
    }

    suspend fun getPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?,
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

    suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethodType: PaymentMethodType,
        pollingDelay: Duration,
        onCheck: (suspend () -> Unit)? = null,
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

    suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        api.updateOrderPaymentStatus(orderId, paymentMethodType)
    }

    suspend fun applyGiftCertificate(
        giftCertificate: GiftCertificate,
        cartFinalPrice: Int,
        cartType: CartType,
    ) {
        api.applyGiftCertificate(
            giftCertificate = giftCertificate,
            cartFinalPrice = cartFinalPrice,
            cartType = cartType,
        )
    }

    suspend fun removeGiftCertificate(paymentMethodType: PaymentMethodType) {
        api.removeGiftCertificate(paymentMethodType)
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

    companion object {
        private const val PAYMENT_RESULT_CHECK_MAX_ERROR_COUNT = 2
    }
}
