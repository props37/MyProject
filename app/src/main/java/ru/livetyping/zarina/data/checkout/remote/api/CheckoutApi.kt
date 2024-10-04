package ru.livetyping.zarina.data.checkout.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.data.cart.remote.api.dto.CartTypeDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.CheckoutCartDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.CheckoutCartRequestBody
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PaymentMethodDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDetailsDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.remote.api.dto.StoreDto
import ru.livetyping.zarina.data.order.remote.api.dto.DeliveryMethodTypeDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class CheckoutApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
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

    suspend fun getCart(checkoutParams: CheckoutParams): CheckoutCartDto {
        val body = CheckoutCartRequestBody.from(checkoutParams)
        return httpClient.get("/api/cart") {
            parameter("cart_type", CartTypeDto.from(checkoutParams.cartType).value)
            parameter("city_kladr_id", checkoutParams.cityKladrId.value)
            if (checkoutParams is StorePickupCheckoutParams) {
                parameter("store_id", checkoutParams.store.id.value)
            }
            parameter("shipping", Json.encodeToString(body))
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
}
