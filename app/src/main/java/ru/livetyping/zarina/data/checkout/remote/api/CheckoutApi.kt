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
import ru.livetyping.zarina.data.checkout.remote.api.dto.CardPaymentDataDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.CardPaymentDataRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.CardPaymentResultDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.CheckoutCartDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.CheckoutCartRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PaymentMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDetailsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.RemoveGiftCertificateRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.StoreDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.UpdateOrderPodeliPaymentStatusRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.exception.ApplyGiftCertificateApiExceptionConverter
import ru.livetyping.zarina.data.order.remote.api.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.data.order.remote.api.dto.PaymentMethodTypeDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CardPaymentData
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.geography.KladrId
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
    suspend fun getPickupStores(cityKladrId: KladrId): List<StoreDto> {
        return httpClient.get("/api/v1/shipping-methods/shops") {
            parameter("city_kladr_id", cityKladrId.value)
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

    suspend fun getCardPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        pickupStoreId: Store.Id?,
    ): CardPaymentDataDto {
        val paymentMethodPath = getCardPaymentMethodPath(paymentMethodType)
        val parametersDto = CardPaymentDataRequestBody(
            products = cart.products.map { CardPaymentDataRequestBody.Product.from(it) },
            finalPrice = cart.price.finalPrice,
            userId = userId?.value,
            storeId = pickupStoreId?.value,
        )
        val parametersString = Json.encodeToString(parametersDto)
        return httpClient.get("/api/$paymentMethodPath/get-link") {
            parameter("data", parametersString)
        }.body()
    }

    suspend fun getCardPaymentResult(
        paymentMethodType: PaymentMethodType,
        paymentData: CardPaymentData,
    ): CardPaymentResultDto {
        val paymentMethodPath = getCardPaymentMethodPath(paymentMethodType)
        val paymentId = paymentData.paymentId.value
        return httpClient
            .get("/api/$paymentMethodPath/check-payment-status/$paymentId/")
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
        certificateNumber: String,
        certificateVerificationCode: String,
        cartFinalPrice: Int,
        cartType: CartType,
    ) {
        val body = ApplyGiftCertificateRequestBody(
            certificateNumber = certificateNumber,
            certificateVerificationCode = certificateVerificationCode,
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

    private fun getCardPaymentMethodPath(paymentMethodType: PaymentMethodType): String {
        return when (paymentMethodType) {
            PaymentMethodType.PAYTURE_WALLET -> "payture-wallet"
            PaymentMethodType.PAYTURE_IN_PAY -> "payture-inpay"
            else -> error("Unsupported payment method type $paymentMethodType")
        }
    }
}
