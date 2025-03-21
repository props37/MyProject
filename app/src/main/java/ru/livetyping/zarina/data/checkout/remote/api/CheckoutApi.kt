package ru.livetyping.zarina.data.checkout.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.data.cart.remote.api.dto.CartTypeDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.ApplyGiftCertificateRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.CheckoutCartDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.CheckoutCartRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PaymentMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PayturePaymentDataDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PayturePaymentDataRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.PayturePaymentResultDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDetailsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.RemoveGiftCertificateRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.SberPaymentDataDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.SberPaymentDataRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.SberPaymentResultDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.StoreDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.UpdateOrderPodeliPaymentStatusRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.exception.ApplyGiftCertificateApiExceptionConverter
import ru.livetyping.zarina.data.order.remote.api.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.data.order.remote.api.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PayturePaymentData
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.SberPaymentData
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.giftcert.GiftCertificate
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.PaymentMethodType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.domain.user.User
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class CheckoutApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val applyGiftCertificateApiExceptionConverter: ApplyGiftCertificateApiExceptionConverter,
) {
    suspend fun getPickupStores(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType,
    ): List<StoreDto> {
        return httpClient.get("/api/v1/shipping-methods/shops") {
            parameter("city_kladr_id", cityKladrId.value)
            parameter("shipping", DeliveryMethodTypeDto.from(deliveryMethodType).value)
        }.body()
    }

    suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto> {
        return httpClient.get("/api/shipping-methods") {
            parameter("cart_type", CartTypeDto.from(cartType).value)
            parameter("address_kladr", cityKladrId.value)
        }.body()
    }

    suspend fun getCourierDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto {
        return httpClient.get("/api/shipping-methods/express") {
            parameter("address_kladr", buildingKladrId.value)
        }.body()
    }

    suspend fun getPostDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto {
        return httpClient.get("/api/shipping-methods/post") {
            parameter("address_kladr", buildingKladrId.value)
        }.body()
    }

    suspend fun getPickupPoints(cityKladrId: KladrId): List<PickupPointDto> {
        return httpClient.get("/api/shipping-methods/pickup_points") {
            parameter("city_kladr_id", cityKladrId.value)
        }.body()
    }

    suspend fun getPickupPointDetails(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): PickupPointDetailsDto {
        // TODO: [High] Always use payment_method=paytureinpay?
        return httpClient.get(
            "/api/shipping-methods/cities/${cityKladrId.value}/pickup_points/" +
                    "${pickupPointId.value}/?payment_method=paytureinpay"
        ).body()
    }

    suspend fun getCart(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): CheckoutCartDto {
        val body = CheckoutCartRequestBody.from(checkoutParams)
        return httpClient.get("/api/cart") {
            parameter("cart_type", CartTypeDto.from(checkoutParams.cartType).value)
            parameter("city_kladr_id", checkoutParams.cityKladrId.value)
            if (checkoutParams is StorePickupCheckoutParams) {
                parameter("store_id", checkoutParams.store.id.value)
            }
            parameter("shipping", Json.encodeToString(body))
            if (paymentMethod != null) {
                parameter("payment_method", PaymentMethodTypeDto.from(paymentMethod.type).value)
            }
        }.body()
    }

    suspend fun getPaymentMethods(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): List<PaymentMethodDto> {
        return httpClient.get("/api/payment-methods/") {
            parameter(
                "shipping_method",
                DeliveryMethodTypeDto.from(checkoutParams.deliveryMethodType).value,
            )
            parameter("order_price", cart.price.cartPrice)
            if (checkoutParams is StorePickupCheckoutParams) {
                parameter("shop", checkoutParams.store.id.value)
            }
        }.body()
    }

    suspend fun getPayturePaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        pickupStoreId: Store.Id?,
    ): PayturePaymentDataDto {
        check(
            paymentMethodType == PaymentMethodType.PAYTURE_WALLET
                    || paymentMethodType == PaymentMethodType.PAYTURE_IN_PAY
        ) { "PaymentMethodType must be ${PaymentMethodType.PAYTURE_IN_PAY} or ${PaymentMethodType.PAYTURE_WALLET}, but was $paymentMethodType" }

        val paymentMethodPath = getPayturePaymentMethodPath(paymentMethodType)
        val parameterDto = PayturePaymentDataRequestBody(
            products = cart.products.map { PayturePaymentDataRequestBody.Product.from(it) },
            finalPrice = cart.price.finalPrice,
            userId = userId?.value,
            storeId = pickupStoreId?.value,
        )
        val parameterString = Json.encodeToString(parameterDto)
        return httpClient.get("/api/$paymentMethodPath/get-link") {
            parameter("data", parameterString)
        }.body()
    }

    suspend fun getSberPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?,
    ): SberPaymentDataDto {
        check(paymentMethodType == PaymentMethodType.SBER) {
            "PaymentMethodType must be ${PaymentMethodType.SBER}, but was $paymentMethodType"
        }

        val parameterDto = SberPaymentDataRequestBody(
            products = cart.products.map { SberPaymentDataRequestBody.Product.from(it) },
            finalPrice = cart.price.finalPrice,
            userId = userId?.value,
            shipping = DeliveryMethodTypeDto.from(deliveryMethodType),
            storeId = pickupStoreId?.value,
        )
        val parameterString = Json.encodeToString(parameterDto)
        return httpClient.get("/api/sber/pre-auth") {
            parameter("data", parameterString)
        }.body()
    }

    suspend fun getPayturePaymentResult(
        paymentMethodType: PaymentMethodType,
        paymentData: PayturePaymentData,
    ): PayturePaymentResultDto {
        val paymentMethodPath = getPayturePaymentMethodPath(paymentMethodType)
        val paymentId = paymentData.paymentId.value
        return httpClient
            .get("/api/$paymentMethodPath/check-payment-status/$paymentId/")
            .body()
    }

    suspend fun getSberPaymentResult(paymentData: SberPaymentData): SberPaymentResultDto {
        return httpClient
            .get("/api/sber/get-status/${paymentData.sberUid.value}")
            .body()
    }

    suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    ) {
        when (paymentMethodType) {
            PaymentMethodType.PAYTURE_WALLET -> updateOrderPaytureWalletPaymentState(orderId)
            PaymentMethodType.PAYTURE_IN_PAY -> updateOrderPaytureInPayPaymentState(orderId)
            PaymentMethodType.QR -> updateOrderQrPaymentState(orderId)
            PaymentMethodType.PODELI -> updateOrderPodeliPaymentState(orderId)
            else -> error("Unsupported payment method type $paymentMethodType")
        }
    }

    suspend fun applyGiftCertificate(
        giftCertificate: GiftCertificate,
        cartFinalPrice: Int,
        cartType: CartType,
    ) {
        val body = ApplyGiftCertificateRequestBody(
            certificateNumber = giftCertificate.number.value,
            certificateVerificationCode = giftCertificate.verificationCode,
            cartFinalPrice = cartFinalPrice,
            cartType = CartTypeDto.from(cartType),
        )
        applyGiftCertificateApiExceptionConverter {
            httpClient.post("/api/gift-card/apply") {
                setJsonBody(body)
            }
        }
    }

    suspend fun removeGiftCertificate(paymentMethodType: PaymentMethodType) {
        val body = RemoveGiftCertificateRequestBody(
            paymentMethodType = PaymentMethodTypeDto.from(paymentMethodType),
        )
        httpClient.delete("/api/gift-card/cancel") {
            setJsonBody(body)
        }
    }

    private suspend fun updateOrderPaytureWalletPaymentState(orderId: Order.Id) {
        httpClient.get("/api/payture-wallet/check-order-payment-status/${orderId.value}")
    }

    private suspend fun updateOrderPaytureInPayPaymentState(orderId: Order.Id) {
        httpClient.get("/api/payture-inpay/check-order-payment-status/${orderId.value}")
    }

    private suspend fun updateOrderQrPaymentState(orderId: Order.Id) {
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
