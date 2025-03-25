package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.data.order.OrderRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.CheckoutStage
import ru.livetyping.zarina.domain.checkout.PaymentData
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.checkout.UrlPaymentData
import ru.livetyping.zarina.domain.checkout.exception.CartChangedException
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderCreationParams
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.PaymentMethodType
import ru.livetyping.zarina.domain.user.User
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class CheckoutUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val updateOrderPaymentStatusUseCase: UpdateOrderPaymentStatusUseCase,
) : FlowUseCase<CheckoutUseCase.Params, CheckoutStage>() {

    override fun execute(params: Params): Flow<CheckoutStage> {
        return flow {
            val cart = params.cart
            val paymentMethod = params.paymentMethod
            val checkoutParams = params.checkoutParams

            checkCartChanges(
                currentCart = cart,
                paymentMethod = paymentMethod,
                checkoutParams = checkoutParams,
            )

            val user = userRepository.getUserFlow().firstOrNull()

            when (paymentMethod.type) {
                PaymentMethodType.SBER, PaymentMethodType.PAYTURE_WALLET, PaymentMethodType.PAYTURE_IN_PAY -> {
                    checkoutWithCardPayment(
                        cart = cart,
                        paymentMethod = paymentMethod,
                        checkoutParams = checkoutParams,
                        user = user,
                    )
                }

                PaymentMethodType.QR, PaymentMethodType.PODELI -> {
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

    private suspend fun FlowCollector<CheckoutStage>.checkoutWithCardPayment(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
        user: User?
    ) {
        val paymentData = getPaymentData(
            cart = cart,
            paymentMethod = paymentMethod,
            checkoutParams = checkoutParams,
            user = user,
        )
        emit(CheckoutStage.PaymentStarted(paymentData))

        awaitPaymentCompleted(
            paymentData = paymentData,
            paymentMethod = paymentMethod,
            onCheck = { emit(CheckoutStage.PaymentStatusChecked) },
        )
        checkoutRepository.onPaymentCompleted(paymentData)
        emit(CheckoutStage.PaymentCompleted)

        var order = createOrder(
            cart = cart,
            paymentMethod = paymentMethod,
            checkoutParams = checkoutParams,
            paymentData = paymentData,
        )
        updateOrderPaymentStatus(order, paymentMethod)

        AppMetricaHelper.reportOrderConfirmed(order)

        val updatedOrder = getOrder(order.id)
        if (updatedOrder != null) order = updatedOrder

        val checkoutCompleted = CheckoutStage.CheckoutCompleted(
            order = order,
            paymentMethodType = paymentMethod.type,
            shouldUpdateOrderStatus = false,
            shouldAwaitPaymentCompleted = false,
        )
        emit(checkoutCompleted)
    }

    private suspend fun FlowCollector<CheckoutStage>.checkoutWithOptionalPayment(
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

        if (order.paymentUrl != null) {
            val paymentData = UrlPaymentData(order.paymentUrl)
            emit(CheckoutStage.PaymentStarted(paymentData))
        } else {
            Timber.v("Order payment URL is not provided")
        }

        AppMetricaHelper.reportOrderConfirmed(order)

        val checkoutCompleted = CheckoutStage.CheckoutCompleted(
            order = order,
            paymentMethodType = paymentMethod.type,
            shouldUpdateOrderStatus = true,
            shouldAwaitPaymentCompleted = true,
        )
        emit(checkoutCompleted)
    }

    private suspend fun FlowCollector<CheckoutStage>.checkoutWithPaymentUponReceipt(
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

        AppMetricaHelper.reportOrderConfirmed(order)

        val checkoutCompleted = CheckoutStage.CheckoutCompleted(
            order = order,
            paymentMethodType = paymentMethod.type,
            shouldUpdateOrderStatus = false,
            shouldAwaitPaymentCompleted = false,
        )
        emit(checkoutCompleted)
    }

    private suspend fun FlowCollector<CheckoutStage>.checkoutWithGiftCertificatePayment(
        cart: Cart,
        paymentMethod: PaymentMethod,
        availablePaymentMethods: List<PaymentMethod>,
        checkoutParams: CheckoutParams,
        user: User?,
    ) {
        if (cart.price.finalPrice > 0) {
            // User has to pay the remaining amount
            val paymentMethodForRemainingPrice =
                findPaymentMethodForRemainingPriceAfterGiftCertificate(availablePaymentMethods)
            val paymentData = getPaymentData(
                cart = cart,
                paymentMethod = paymentMethodForRemainingPrice,
                checkoutParams = checkoutParams,
                user = user,
            )
            emit(CheckoutStage.PaymentStarted(paymentData))

            awaitPaymentCompleted(
                paymentData = paymentData,
                paymentMethod = paymentMethodForRemainingPrice,
                onCheck = { emit(CheckoutStage.PaymentStatusChecked) },
            )
            checkoutRepository.onPaymentCompleted(paymentData)
            emit(CheckoutStage.PaymentCompleted)

            // Use the original payment method to create an order
            var order = createOrder(
                cart = cart,
                paymentMethod = paymentMethod,
                checkoutParams = checkoutParams,
                paymentData = paymentData,
            )
            // Use the payment method used to pay the remaining amount
            updateOrderPaymentStatus(order, paymentMethodForRemainingPrice)

            AppMetricaHelper.reportOrderConfirmed(order)

            val updatedOrder = getOrder(order.id)
            if (updatedOrder != null) order = updatedOrder

            val checkoutCompleted = CheckoutStage.CheckoutCompleted(
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

            AppMetricaHelper.reportOrderConfirmed(order)

            val checkoutCompleted = CheckoutStage.CheckoutCompleted(
                order = order,
                paymentMethodType = paymentMethod.type,
                shouldUpdateOrderStatus = false,
                shouldAwaitPaymentCompleted = false,
            )
            emit(checkoutCompleted)
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
        val pickupStore = (checkoutParams as? StorePickupCheckoutParams)?.store
        return checkoutRepository.getPaymentData(
            cart = cart,
            paymentMethodType = paymentMethod.type,
            userId = user?.id,
            deliveryMethodType = checkoutParams.deliveryMethodType,
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
            pollingDelay = PAYMENT_RESULT_POLLING_DELAY,
            onCheck = onCheck,
        )
    }

    private suspend fun createOrder(
        cart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
        paymentData: PaymentData?,
    ): OrderDetails {
        val orderCreationParams = OrderCreationParams(
            cart = cart,
            paymentMethodType = paymentMethod.type,
            checkoutParams = checkoutParams,
            paymentData = paymentData,
        )
        return orderRepository.createOrder(orderCreationParams)
    }

    private suspend fun updateOrderPaymentStatus(
        order: Order,
        paymentMethod: PaymentMethod,
    ) {
        val params = UpdateOrderPaymentStatusUseCase.Params(
            orderId = order.id,
            paymentMethodType = paymentMethod.type,
        )
        updateOrderPaymentStatusUseCase(params)
            .onFailure { t ->
                Timber.e(t, "Failed to update order payment status")
            }
    }

    private suspend fun getOrder(id: Order.Id): OrderDetails? {
        return try {
            orderRepository.getOrderFlow(id).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun checkCartChanges(
        currentCart: Cart,
        paymentMethod: PaymentMethod,
        checkoutParams: CheckoutParams,
    ) {
        val newCart = checkoutRepository
            .getCartFlow(
                checkoutParams = checkoutParams,
                paymentMethod = paymentMethod,
            )
            .firstOrNull()
        checkNotNull(newCart) { "Failed to get cart" }

        checkCartEquality { newCart.products.size == currentCart.products.size }
        checkCartEquality { newCart.price == currentCart.price }
        checkCartEquality { newCart.myCard == currentCart.myCard }
        checkCartEquality { newCart.bonuses == currentCart.bonuses }
        checkCartEquality { newCart.promoCode == currentCart.promoCode }
    }

    private fun checkCartEquality(predicate: () -> Boolean) {
        if (!predicate()) throw CartChangedException()
    }

    data class Params(
        val cart: Cart,
        val paymentMethod: PaymentMethod,
        val availablePaymentMethods: List<PaymentMethod>,
        val checkoutParams: CheckoutParams,
    )

    companion object {
        private val PAYMENT_RESULT_POLLING_DELAY = 3.seconds
    }
}
