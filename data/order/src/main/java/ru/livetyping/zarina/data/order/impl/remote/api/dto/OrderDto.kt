package ru.livetyping.zarina.data.order.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.PhoneNumber
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDeliveryInfo
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.domain.model.order.OrderPrice
import ru.livetyping.zarina.core.domain.model.order.OrderRecipient
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import java.time.LocalDate
import java.time.format.DateTimeParseException
import ru.livetyping.zarina.core.domain.model.common.Color as ColorDomain
import ru.livetyping.zarina.core.domain.model.product.Product as ProductDomain

@Serializable
internal data class OrderDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("number")
    val number: String? = null,

    @SerialName("products_count")
    val productCount: Int? = null,

    @SerialName("date")
    val date: String? = null,

    @SerialName("status")
    val status: OrderStatusDto? = null,

    @SerialName("total_sum")
    val totalSum: Int? = null,

    @SerialName("products")
    val products: List<Product>? = null,

    @SerialName("shipping")
    val shipping: DeliveryInfo? = null,

    @SerialName("payment_method")
    val paymentMethod: PaymentMethod? = null,

    @SerialName("payment_tool")
    val paymentTool: PaymentTool? = null,

    @SerialName("contact_info")
    val contactInfo: ContactInfo? = null,

    @SerialName("address")
    val address: String? = null,

    @SerialName("is_cancelable")
    val isCancelable: Boolean? = null,
) {
    fun toOrderDetailed(requireAddress: Boolean = true): OrderDetailed {
        checkPropertyNotNull(id) { ::id }
        checkPropertyNotNull(number) { ::number }
        checkPropertyNotNull(productCount) { ::productCount }
        checkPropertyNotNull(date) { ::date }
        checkPropertyNotNull(status) { ::status }
        checkPropertyNotNull(totalSum) { ::totalSum }
        checkPropertyNotNull(products) { ::products }
        checkPropertyNotNull(shipping) { ::shipping }
        checkPropertyNotNull(shipping.shippingMethod) { shipping::shippingMethod }
        checkPropertyNotNull(paymentMethod) { ::paymentMethod }
        checkPropertyNotNull(paymentMethod.code) { paymentMethod::code }
        checkPropertyNotNull(contactInfo) { ::contactInfo }
        if (requireAddress) {
            checkPropertyNotNull(address) { ::address }
        }
        val date = try {
            LocalDate.parse(date)
        } catch (e: DateTimeParseException) {
            LocalDate.parse(date.substringBefore('T'))
        }
        // TODO: [Backend] Migrate to separate field when it is available
        val deliveryPrice = shipping.shippingMethod.price?.toInt() ?: 0
        val price = OrderPrice(
            orderPrice = totalSum - deliveryPrice,
            deliveryPrice = deliveryPrice,
            totalPrice = totalSum,
        )
        return OrderDetailed(
            id = Order.Id(id.toString()),
            number = Order.Number(number),
            productCount = productCount,
            date = date,
            status = status.toOrderStatus(),
            products = products.map { it.toOrderProduct() },
            price = price,
            paymentMethodType = paymentMethod.code.toPaymentMethodType(),
            paymentUrl = paymentTool?.link?.let { Url.create(it) },
            deliveryInfo = shipping.toOrderDeliveryInfo(),
            recipient = contactInfo.toOrderRecipient(),
            deliveryAddress = address.orEmpty(),
            isCancellable = isCancelable ?: false,
        )
    }

    @Serializable
    data class Product(
        @SerialName("vendor_code")
        val vendorCode: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("size")
        val size: String? = null,

        @SerialName("color")
        val color: Color? = null,

        @SerialName("cover_picture")
        val coverPicture: String? = null,

        @SerialName("price")
        val price: Int? = null,

        @SerialName("quantity")
        val quantity: Int? = null,
    ) {
        fun toOrderProduct(): OrderDetailed.Product {
            checkPropertyNotNull(vendorCode) { ::vendorCode }
            checkPropertyNotNull(name) { ::name }
            checkPropertyNotNull(size) { ::size }
            checkPropertyNotNull(color) { ::color }
            checkPropertyNotNull(coverPicture) { ::coverPicture }
            checkPropertyNotNull(price) { ::price }
            checkPropertyNotNull(quantity) { ::quantity }
            return OrderDetailed.Product(
                id = ProductDomain.Id(vendorCode),
                name = name,
                size = size,
                color = getProductColor(),
                imageUrl = Url.create(coverPicture),
                price = ProductPrice(price),
                count = quantity,
            )
        }

        private fun getProductColor(): ProductColor {
            checkPropertyNotNull(color) { ::color }
            checkPropertyNotNull(color.code) { color::code }
            checkPropertyNotNull(color.title) { color::title }
            checkPropertyNotNull(vendorCode) { ::vendorCode }
            return ProductColor(
                id = ProductColor.Id(color.code),
                name = color.title,
                color = ColorDomain(color.code),
                productId = ProductDomain.Id(vendorCode),
            )
        }

        @Serializable
        data class Color(
            @SerialName("title")
            val title: String? = null,

            @SerialName("code")
            val code: String? = null,
        )
    }

    @Serializable
    data class DeliveryInfo(
        @SerialName("shipping_method")
        val shippingMethod: Method? = null,
    ) {
        fun toOrderDeliveryInfo(): OrderDeliveryInfo {
            checkPropertyNotNull(shippingMethod) { ::shippingMethod }
            checkPropertyNotNull(shippingMethod.type) { shippingMethod::type }
            return OrderDeliveryInfo(
                type = shippingMethod.type.toDeliveryMethodType(),
            )
        }

        @Serializable
        data class Method(
            @SerialName("type")
            val type: DeliveryMethodTypeDto? = null,

            @SerialName("price")
            val price: Float? = null,
        )
    }

    @Serializable
    data class PaymentMethod(
        @SerialName("code")
        val code: PaymentMethodTypeDto? = null,
    )

    @Serializable
    data class PaymentTool(
        @SerialName("link") 
        val link: String? = null,
    )

    @Serializable
    data class ContactInfo(
        @SerialName("first_name")
        val firstName: String? = null,

        @SerialName("last_name")
        val lastName: String? = null,

        @SerialName("email")
        val email: String? = null,

        @SerialName("phone")
        val phone: String? = null,
    ) {
        fun toOrderRecipient(): OrderRecipient {
            checkPropertyNotNull(firstName) { ::firstName }
            checkPropertyNotNull(email) { ::email }
            return OrderRecipient(
                firstName = firstName,
                lastName = lastName,
                email = Email.create(email),
                phone = phone?.let { PhoneNumber.create(it) },
            )
        }
    }
}
