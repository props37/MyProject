package ru.livetyping.zarina.data.checkout.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.OrderCreationParams
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PayturePaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.SberPaymentData
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.core.network.zarina.dto.CartTypeDto
import ru.livetyping.zarina.core.network.zarina.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.core.network.zarina.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.CheckoutCartDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.CheckoutCartRequestBody
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.CreateOrderRequestBody
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.CreatedOrderDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PaymentMethodDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PayturePaymentDataDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PayturePaymentDataRequestBody
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PayturePaymentResultDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupPointDetailedDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupStoreDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.SberPaymentDataDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.SberPaymentDataRequestBody
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.SberPaymentResultDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.UpdateOrderPodeliPaymentStatusRequestBody
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.WithdrawGiftCertificateRequestBody
import ru.livetyping.zarina.data.checkout.impl.remote.api.exception.OrderCreationExceptionConverter
import javax.inject.Inject

internal class CheckoutApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val orderCreationExceptionConverter: OrderCreationExceptionConverter,
) : CheckoutApi {
    override suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType) {
        val body = WithdrawGiftCertificateRequestBody(
            paymentMethodType = PaymentMethodTypeDto.from(paymentMethodType),
        )
        httpClient.delete("/api/gift-card/cancel") {
            setJsonBody(body)
        }
    }

    override suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto> {
        return httpClient.get("/api/shipping-methods") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
            parameter("address_kladr", cityKladrId.value)
        }.body()
    }

    override suspend fun getPickupPoints(cityKladrId: KladrId): List<PickupPointDto> {
        return httpClient.get("/api/shipping-methods/pickup_points") {
            parameter("city_kladr_id", cityKladrId.value)
        }.body()
    }

    override suspend fun getPickupPoint(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): PickupPointDetailedDto {
        // Why do we always use payment_method=paytureinpay?
        return httpClient.get(
            "/api/shipping-methods/cities/${cityKladrId.value}/pickup_points/" +
                    "${pickupPointId.value}/?payment_method=paytureinpay"
        ).body()
    }

    override suspend fun getPickupStores(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType
    ): List<PickupStoreDto> {
        return httpClient.get("/api/v1/shipping-methods/shops") {
            parameter("city_kladr_id", cityKladrId.value)
            parameter("shipping", DeliveryMethodTypeDto.from(deliveryMethodType).value)
        }.body()
    }

    override suspend fun getCourierDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto {
        return httpClient.get("/api/shipping-methods/express") {
            parameter("address_kladr", buildingKladrId.value)
        }.body()
    }

    override suspend fun getPostDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto {
        return httpClient.get("/api/shipping-methods/post") {
            parameter("address_kladr", buildingKladrId.value)
        }.body()
    }

    override suspend fun getCart(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): CheckoutCartDto {
        val body = CheckoutCartRequestBody.from(checkoutParams)
        return httpClient.get("/api/cart") {
            parameter("cart_type", CartTypeDto.from(checkoutParams.cartType).value)
            parameter("city_kladr_id", checkoutParams.cityKladrId.value)
            if (checkoutParams is PickupFromStoreCheckoutParams) {
                parameter("store_id", checkoutParams.store.id.value)
            }
            parameter("shipping", Json.encodeToString(body))
            if (paymentMethod != null) {
                parameter("payment_method", PaymentMethodTypeDto.from(paymentMethod.type).value)
            }
        }.body()
    }

    override suspend fun getPaymentMethods(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): List<PaymentMethodDto> {
        return httpClient.get("/api/payment-methods/") {
            parameter(
                key = "shipping_method",
                value = DeliveryMethodTypeDto.from(checkoutParams.deliveryMethod.type).value,
            )
            parameter("order_price", cart.price.cartPrice)
            if (checkoutParams is PickupFromStoreCheckoutParams) {
                parameter("shop", checkoutParams.store.id.value)
            }
        }.body()
    }

    override suspend fun getPayturePaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        pickupStoreId: Store.Id?
    ): PayturePaymentDataDto {
        check(
            paymentMethodType == PaymentMethodType.PAYTURE_WALLET
                    || paymentMethodType == PaymentMethodType.PAYTURE_IN_PAY
        ) { "PaymentMethodType must be ${PaymentMethodType.PAYTURE_IN_PAY} or ${PaymentMethodType.PAYTURE_WALLET}, but was $paymentMethodType" }

        val paymentMethodPath = getPayturePaymentMethodPath(paymentMethodType)
        val parameterDto = PayturePaymentDataRequestBody(
            products = cart.products.map { PayturePaymentDataRequestBody.Product.from(it) },
            finalPrice = cart.price.finalPrice.toInt(),
            userId = userId?.value,
            storeId = pickupStoreId?.value,
        )
        val parameterString = Json.encodeToString(parameterDto)
        return httpClient.get("/api/$paymentMethodPath/get-link") {
            parameter("data", parameterString)
        }.body()
    }

    override suspend fun getSberPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?
    ): SberPaymentDataDto {
        check(paymentMethodType == PaymentMethodType.SBER) {
            "PaymentMethodType must be ${PaymentMethodType.SBER}, but was $paymentMethodType"
        }

        val parameterDto = SberPaymentDataRequestBody(
            products = cart.products.map { SberPaymentDataRequestBody.Product.from(it) },
            finalPrice = cart.price.finalPrice.toInt(),
            userId = userId?.value,
            shipping = DeliveryMethodTypeDto.from(deliveryMethodType),
            storeId = pickupStoreId?.value,
        )
        val parameterString = Json.encodeToString(parameterDto)
        return httpClient.get("/api/sber/pre-auth") {
            parameter("data", parameterString)
        }.body()
    }

    override suspend fun createOrder(params: OrderCreationParams): CreatedOrderDto {
        val body = CreateOrderRequestBody.from(params)
        return orderCreationExceptionConverter {
            httpClient.post("/api/orders/") {
                setJsonBody(body)
            }.body()
        }
    }

    override suspend fun getPayturePaymentResult(
        paymentMethodType: PaymentMethodType,
        paymentData: PayturePaymentData
    ): PayturePaymentResultDto {
        val paymentMethodPath = getPayturePaymentMethodPath(paymentMethodType)
        val paymentId = paymentData.paymentId.value
        return httpClient
            .get("/api/$paymentMethodPath/check-payment-status/$paymentId/")
            .body()
    }

    override suspend fun getSberPaymentResult(paymentData: SberPaymentData): SberPaymentResultDto {
        return httpClient
            .get("/api/sber/get-status/${paymentData.sberUid.value}")
            .body()
    }

    override suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        when (paymentMethodType) {
            PaymentMethodType.SBER, PaymentMethodType.SBER_SBP -> {
                updateOrderSberPaymentState(orderId)
            }

            PaymentMethodType.PAYTURE_WALLET -> updateOrderPaytureWalletPaymentState(orderId)
            PaymentMethodType.PAYTURE_IN_PAY -> updateOrderPaytureInPayPaymentState(orderId)
            PaymentMethodType.SBP -> updateOrderSbpPaymentState(orderId)
            PaymentMethodType.PODELI -> updateOrderPodeliPaymentState(orderId)
            else -> error("Unsupported payment method type $paymentMethodType")
        }
    }

    private suspend fun updateOrderSberPaymentState(orderId: Order.Id) {
        httpClient.get("/api/sber/get-order-status/${orderId.value}")
    }

    private suspend fun updateOrderPaytureWalletPaymentState(orderId: Order.Id) {
        httpClient.get("/api/payture-wallet/check-order-payment-status/${orderId.value}")
    }

    private suspend fun updateOrderPaytureInPayPaymentState(orderId: Order.Id) {
        httpClient.get("/api/payture-inpay/check-order-payment-status/${orderId.value}")
    }

    private suspend fun updateOrderSbpPaymentState(orderId: Order.Id) {
        httpClient.get("/api/payment/check/qrcode/${orderId.value}")
    }

    private suspend fun updateOrderPodeliPaymentState(orderId: Order.Id) {
        val body = UpdateOrderPodeliPaymentStatusRequestBody(orderId.value)
        httpClient.post("/api/podeli/check-status") {
            setJsonBody(body)
        }
    }

    private fun getPayturePaymentMethodPath(paymentMethodType: PaymentMethodType): String {
        return when (paymentMethodType) {
            PaymentMethodType.PAYTURE_WALLET -> "payture-wallet"
            PaymentMethodType.PAYTURE_IN_PAY -> "payture-inpay"
            else -> error("Unsupported payment method type $paymentMethodType")
        }
    }
}
