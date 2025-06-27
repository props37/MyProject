package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.analytics.toAppMetricaOrder
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutStep
import ru.livetyping.zarina.core.domain.model.checkout.OrderCreationParams
import ru.livetyping.zarina.core.domain.model.checkout.PaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.SberSbpPaymentData
import ru.livetyping.zarina.core.domain.model.checkout.UrlPaymentData
import ru.livetyping.zarina.core.domain.model.checkout.exception.CartChangedException
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderStatus
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.OrderRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.CheckoutUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import java.math.BigDecimal
import kotlin.time.Duration.Companion.milliseconds

internal class CheckoutUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val appMetrica: AppMetrica,
    private val logger: UseCaseLogger?,
) : FlowUseCase<Params, CheckoutStep>(logger), CheckoutUseCase {

    override fun execute(params: Params): Flow<CheckoutStep> {
        return flow {
            val cart = params.cart
            val paymentMethod = params.paymentMethod
            val checkoutParams = params.checkoutParams

            checkCartChanges(
                currentCart = cart,
                paymentMethod = paymentMethod,
                checkoutParams = checkoutParams,
            )

            val user = userRepository.getUserFlow(CachePolicy.LocalOnly).firstOrNull()

            when (paymentMethod.type) {
                PaymentMethodType.SBER, PaymentMethodType.PAYTURE_WALLET, PaymentMethodType.PAYTURE_IN_PAY -> {
                    checkoutWithCardPayment(
                        cart = cart,
                        paymentMethod = paymentMethod,
                        checkoutParams = checkoutParams,
                        user = user,
                    )
                }

                PaymentMethodType.SBER_SBP, PaymentMethodType.SBP, PaymentMethodType.PODELI -> {
                    checkoutWithOptionalPayment(
                        cart = cart,
                        paymentMethod = paymentMethod,
                        checkoutParams = checkoutParams,
                    )
                }

                PaymentMethodType.POSTPAID -> {
                    checkoutWithPaymentUponReceipt(
                        cart = cart,
                        paymentMethod = paymentMethod,
                        checkoutParams = checkoutParams,
                    )
                }

                PaymentMethodType.GIFT_CERTIFICATE -> {
                    checkoutWithGiftCertificatePayment(
                        cart = cart,
                        paymentMethod = paymentMethod,
                        availablePaymentMethods = params.availablePaymentMethods,
                        checkoutParams = checkoutParams,
                        user = user,
                    )
                }

                else -> error("Unsupported payment method type ${paymentMethod.type}")
            }
        }
    }

    override fun invoke(params: Params): Flow<Result<CheckoutStep>> {
        return call(params)
    }

    private suspend fun FlowCollector<CheckoutStep>.checkoutWithCardPayment(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
        user: User?,
    ) {
        val paymentData = getPaymentData(
            cart = cart,
            paymentMethod = paymentMethod,
            checkoutParams = checkoutParams,
            user = user,
        )
        emit(CheckoutStep.PaymentStarted(paymentData))

        awaitPaymentCompleted(
            paymentData = paymentData,
            paymentMethod = paymentMethod,
            onCheck = { emit(CheckoutStep.PaymentStatusChecked) },
        )
        checkoutRepository.onPaymentCompleted(paymentData)
        emit(CheckoutStep.PaymentCompleted)

        var order = createOrder(
            cart = cart,
            paymentMethod = paymentMethod,
            checkoutParams = checkoutParams,
            paymentData = paymentData,
        )
        updateOrderPaymentStatus(order, paymentMethod)

        appMetrica.reportOrderConfirmed(order.toAppMetricaOrder())

        val updatedOrderStatus = getOrderStatus(order.id)
        if (updatedOrderStatus != null) {
            order = order.copy(status = updatedOrderStatus)
        }

        val checkoutCompleted = CheckoutStep.CheckoutCompleted(
            order = order,
            paymentMethodType = paymentMethod.type,
            shouldUpdateOrderStatus = false,
            shouldAwaitPaymentCompleted = false,
        )
        emit(checkoutCompleted)
    }

    private suspend fun FlowCollector<CheckoutStep>.checkoutWithOptionalPayment(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
    ) {
        val order = createOrder(
            cart = cart,
            paymentMethod = paymentMethod,
            checkoutParams = checkoutParams,
            paymentData = null,
        )

        var shouldAwaitPaymentCompleted = true

        if (order.paymentUrl != null) {
            val paymentData = getOptionalPaymentData(paymentMethod, order.paymentUrl, order.number)
            emit(CheckoutStep.PaymentStarted(paymentData))

            if (paymentData is SberSbpPaymentData) {
                shouldAwaitPaymentCompleted = false
            }

            awaitPaymentCompleted(
                paymentData = paymentData,
                paymentMethod = paymentMethod,
                onCheck = { emit(CheckoutStep.PaymentStatusChecked) },
            )
            checkoutRepository.onPaymentCompleted(paymentData)
            emit(CheckoutStep.PaymentCompleted)
        } else {
            logger?.v(TAG, "Order payment URL is not provided")
        }

        appMetrica.reportOrderConfirmed(order.toAppMetricaOrder())

        val checkoutCompleted = CheckoutStep.CheckoutCompleted(
            order = order,
            paymentMethodType = paymentMethod.type,
            shouldUpdateOrderStatus = true,
            shouldAwaitPaymentCompleted = shouldAwaitPaymentCompleted,
        )
        emit(checkoutCompleted)
    }

    private suspend fun FlowCollector<CheckoutStep>.checkoutWithPaymentUponReceipt(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
    ) {
        val order = createOrder(
            cart = cart,
            paymentMethod = paymentMethod,
            checkoutParams = checkoutParams,
            paymentData = null,
        )

        appMetrica.reportOrderConfirmed(order.toAppMetricaOrder())

        val checkoutCompleted = CheckoutStep.CheckoutCompleted(
            order = order,
            paymentMethodType = paymentMethod.type,
            shouldUpdateOrderStatus = false,
            shouldAwaitPaymentCompleted = false,
        )
        emit(checkoutCompleted)
    }

    private suspend fun FlowCollector<CheckoutStep>.checkoutWithGiftCertificatePayment(
        cart: Cart,
        paymentMethod: PaymentMethod,
        availablePaymentMethods: List<PaymentMethod>,
        checkoutParams: CheckoutParams,
        user: User?,
    ) {
        if (cart.price.finalPrice > BigDecimal.ZERO) {
            // User has to pay the remaining amount
            val paymentMethodForRemainingPrice =
                findPaymentMethodForRemainingPriceAfterGiftCertificate(availablePaymentMethods)
            val paymentData = getPaymentData(
                cart = cart,
                paymentMethod = paymentMethodForRemainingPrice,
                checkoutParams = checkoutParams,
                user = user,
            )
            emit(CheckoutStep.PaymentStarted(paymentData))

            awaitPaymentCompleted(
                paymentData = paymentData,
                paymentMethod = paymentMethodForRemainingPrice,
                onCheck = { emit(CheckoutStep.PaymentStatusChecked) },
            )
            checkoutRepository.onPaymentCompleted(paymentData)
            emit(CheckoutStep.PaymentCompleted)

            // Use the original payment method to create an order
            var order = createOrder(
                cart = cart,
                paymentMethod = paymentMethod,
                checkoutParams = checkoutParams,
                paymentData = paymentData,
            )
            // Use the payment method used to pay the remaining amount
            updateOrderPaymentStatus(order, paymentMethodForRemainingPrice)

            appMetrica.reportOrderConfirmed(order.toAppMetricaOrder())

            val updatedOrderStatus = getOrderStatus(order.id)
            if (updatedOrderStatus != null) {
                order = order.copy(status = updatedOrderStatus)
            }

            val checkoutCompleted = CheckoutStep.CheckoutCompleted(
                order = order,
                paymentMethodType = paymentMethod.type,
                shouldUpdateOrderStatus = false,
                shouldAwaitPaymentCompleted = false,
            )
            emit(checkoutCompleted)
        } else {
            // The gift certificate is enough, there is no remaining price the user has to pay
            val order = createOrder(
                cart = cart,
                paymentMethod = paymentMethod,
                checkoutParams = checkoutParams,
                paymentData = null,
            )

            appMetrica.reportOrderConfirmed(order.toAppMetricaOrder())

            val checkoutCompleted = CheckoutStep.CheckoutCompleted(
                order = order,
                paymentMethodType = paymentMethod.type,
                shouldUpdateOrderStatus = false,
                shouldAwaitPaymentCompleted = false,
            )
            emit(checkoutCompleted)
        }
    }

    private fun getOptionalPaymentData(
        paymentMethod: PaymentMethod,
        paymentUrl: Url,
        orderNumber: Order.Number,
    ): PaymentData {
        return if (paymentMethod.type == PaymentMethodType.SBER_SBP) {
            SberSbpPaymentData(orderNumber, paymentUrl)
        } else {
            UrlPaymentData(paymentUrl)
        }
    }

    private fun findPaymentMethodForRemainingPriceAfterGiftCertificate(
        availablePaymentMethods: List<PaymentMethod>,
    ): PaymentMethod {
        val paymentMethod = availablePaymentMethods.find {
            it.type == PaymentMethodType.SBER
        }
        checkNotNull(paymentMethod) {
            "Failed to find payment method for remaining price after gift certificate"
        }
        return paymentMethod
    }

    private suspend fun getPaymentData(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
        user: User?,
    ): PaymentData {
        val pickupStore = (checkoutParams as? PickupFromStoreCheckoutParams)?.store
        return checkoutRepository.getPaymentData(
            cart = cart,
            paymentMethodType = paymentMethod.type,
            userId = user?.id,
            deliveryMethodType = checkoutParams.deliveryMethod.type,
            pickupStoreId = pickupStore?.id,
        )
    }

    private suspend fun awaitPaymentCompleted(
        paymentData: PaymentData,
        paymentMethod: PaymentMethod,
        onCheck: (suspend () -> Unit)? = null,
    ) {
        checkoutRepository.awaitPaymentCompleted(
            paymentMethodType = paymentMethod.type,
            paymentData = paymentData,
            pollingDelay = PAYMENT_RESULT_POLLING_DELAY_MILLIS.milliseconds,
            onCheck = onCheck,
        )
    }

    private suspend fun createOrder(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
        paymentData: PaymentData?,
    ): OrderDetailed {
        val orderCreationParams = OrderCreationParams(
            cart = cart,
            paymentMethodType = paymentMethod.type,
            checkoutParams = checkoutParams,
            paymentData = paymentData,
        )
        return checkoutRepository.createOrder(orderCreationParams)
    }

    private suspend fun updateOrderPaymentStatus(
        order: Order,
        paymentMethod: PaymentMethod,
    ) {
        try {
            checkoutRepository.updateOrderPaymentStatus(order.id, paymentMethod.type)
        } catch (e: Exception) {
            logger?.e(TAG, e, "Failed to update order payment status")
        }
    }

    private suspend fun getOrderStatus(id: Order.Id): OrderStatus? {
        return try {
            orderRepository.getOrderStatus(id)
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun checkCartChanges(
        currentCart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
    ) {
        val newCart = checkoutRepository.getCartFlow(checkoutParams, paymentMethod).firstOrNull()
        checkNotNull(newCart) { "Failed to get cart" }

        checkCartEquality { newCart.products.size == currentCart.products.size }
        checkCartEquality { newCart.price == currentCart.price }
        checkCartEquality { newCart.myCard == currentCart.myCard }
        checkCartEquality { newCart.bonusAccount == currentCart.bonusAccount }
        checkCartEquality { newCart.promoCode == currentCart.promoCode }
    }

    private fun checkCartEquality(predicate: () -> Boolean) {
        if (!predicate()) throw CartChangedException()
    }

    private companion object {
        private const val PAYMENT_RESULT_POLLING_DELAY_MILLIS = 3000L

        private const val TAG = "CheckoutFlowUseCaseImpl"
    }
}
