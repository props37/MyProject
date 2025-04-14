package ru.livetyping.zarina.data.checkout.impl.remote.api

import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.checkout.CheckoutParams
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethodType
import ru.livetyping.zarina.core.domain.model.checkout.OrderCreationParams
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.checkout.PayturePaymentData
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.domain.model.checkout.SberPaymentData
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.CheckoutCartDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.CreatedOrderDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryMethodDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.DeliveryOptionsDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PaymentMethodDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PayturePaymentDataDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PayturePaymentResultDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupPointDetailedDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupPointDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.PickupStoreDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.SberPaymentDataDto
import ru.livetyping.zarina.data.checkout.impl.remote.api.dto.SberPaymentResultDto

internal interface CheckoutApi {
    suspend fun withdrawGiftCertificate(paymentMethodType: PaymentMethodType)

    suspend fun getDeliveryMethods(
        cartType: CartType,
        cityKladrId: KladrId,
    ): List<DeliveryMethodDto>

    suspend fun getPickupPoints(cityKladrId: KladrId): List<PickupPointDto>

    suspend fun getPickupPoint(
        cityKladrId: KladrId,
        pickupPointId: PickupPoint.Id,
    ): PickupPointDetailedDto

    suspend fun getPickupStores(
        cityKladrId: KladrId,
        deliveryMethodType: DeliveryMethodType,
    ): List<PickupStoreDto>

    suspend fun getCourierDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto

    suspend fun getPostDeliveryOptions(buildingKladrId: KladrId): DeliveryOptionsDto

    suspend fun getCart(
        checkoutParams: CheckoutParams,
        paymentMethod: PaymentMethod?,
    ): CheckoutCartDto

    suspend fun getPaymentMethods(
        checkoutParams: CheckoutParams,
        cart: Cart,
    ): List<PaymentMethodDto>

    suspend fun getPayturePaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        pickupStoreId: Store.Id?,
    ): PayturePaymentDataDto

    suspend fun getSberPaymentData(
        cart: Cart,
        paymentMethodType: PaymentMethodType,
        userId: User.Id?,
        deliveryMethodType: DeliveryMethodType,
        pickupStoreId: Store.Id?,
    ): SberPaymentDataDto

    suspend fun createOrder(params: OrderCreationParams): CreatedOrderDto

    suspend fun getPayturePaymentResult(
        paymentMethodType: PaymentMethodType,
        paymentData: PayturePaymentData,
    ): PayturePaymentResultDto

    suspend fun getSberPaymentResult(paymentData: SberPaymentData): SberPaymentResultDto

    suspend fun updateOrderPaymentStatus(
        orderId: Order.Id,
        paymentMethodType: PaymentMethodType,
    )
}
