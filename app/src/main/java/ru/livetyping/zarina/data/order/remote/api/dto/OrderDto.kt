package ru.livetyping.zarina.data.order.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderContactInfo
import ru.livetyping.zarina.domain.order.OrderDeliveryInfo
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.OrderPrice
import ru.livetyping.zarina.domain.product.Price
import ru.livetyping.zarina.domain.product.ProductColor
import java.time.LocalDate
import ru.livetyping.zarina.domain.common.Color as DomainColor
import ru.livetyping.zarina.domain.product.Product as DomainProduct

@Serializable
data class OrderDto(
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
    val totalPrice: Long? = null,

    @SerialName("products")
    val products: List<Product>? = null,

    @SerialName("shipping")
    val deliveryInfo: DeliveryInfo? = null,

    @SerialName("payment_method")
    val paymentMethod: PaymentMethod? = null,

    @SerialName("contact_info")
    val contactInfo: ContactInfo? = null,
) {
    fun toOrderDetails(): OrderDetails {
        checkNotNull(id) { "id is null" }
        checkNotNull(number) { "number is null" }
        checkNotNull(productCount) { "productCount is null" }
        checkNotNull(date) { "date is null" }
        checkNotNull(status) { "status is null" }
        checkNotNull(totalPrice) { "totalPrice is null" }
        checkNotNull(products) { "products is null" }
        checkNotNull(deliveryInfo) { "deliveryInfo is null" }
        checkNotNull(deliveryInfo.method) { "deliveryInfo method is null" }
        checkNotNull(paymentMethod) { "paymentMethod is null" }
        checkNotNull(paymentMethod.method) { "paymentMethod method is null" }
        checkNotNull(contactInfo) { "contactInfo is null" }
        // TODO: [High] Migrate to separate field when it is available
        val deliveryPrice = deliveryInfo.method.price ?: 0
        val price = OrderPrice(
            orderPrice = totalPrice - deliveryPrice,
            deliveryPrice = deliveryPrice,
            totalPrice = totalPrice,
        )
        return OrderDetails(
            id = Order.Id(id),
            number = Order.Number(number),
            productCount = productCount,
            date = LocalDate.parse(date),
            status = status.toOrderStatus(),
            products = products.map { it.toOrderProduct() },
            price = price,
            paymentMethod = paymentMethod.method.toOrderPaymentMethod(),
            deliveryInfo = deliveryInfo.toOrderDeliveryInfo(),
            contactInfo = contactInfo.toOrderContactInfo(),
        )
    }

    @Serializable
    data class Product(
        @SerialName("vendor_code")
        val id: String? = null,
        
        @SerialName("name")
        val name: String? = null,

        @SerialName("size")
        val size: String? = null,

        @SerialName("color")
        val color: Color? = null,

        @SerialName("cover_picture")
        val imageUrl: String? = null,

        @SerialName("price")
        val price: Long? = null,

        @SerialName("quantity")
        val count: Int? = null,
    ) {
        fun toOrderProduct(): OrderDetails.Product {
            checkNotNull(id) { "id is null" }
            checkNotNull(name) { "name is null" }
            checkNotNull(size) { "size is null" }
            checkNotNull(color) { "color is null" }
            checkNotNull(imageUrl) { "imageUrl is null" }
            checkNotNull(price) { "price is null" }
            checkNotNull(count) { "count is null" }
            return OrderDetails.Product(
                id = DomainProduct.Id(id),
                name = name,
                size = size,
                color = getProductColor(),
                imageUrl = Url(imageUrl),
                price = Price(price),
                count = count,
            )
        }

        private fun getProductColor(): ProductColor {
            checkNotNull(color) { "color is null" }
            checkNotNull(color.code) { "code is null" }
            checkNotNull(color.name) { "name is null" }
            checkNotNull(id) { "id is null" }
            return ProductColor(
                id = ProductColor.Id(color.code),
                name = color.name,
                color = DomainColor(color.code),
                productId = DomainProduct.Id(id),
            )
        }

        @Serializable
        data class Color(
            @SerialName("title")
            val name: String? = null,

            @SerialName("code")
            val code: String? = null,
        )
    }

    @Serializable
    data class DeliveryInfo(
        @SerialName("shipping_method")
        val method: Method? = null,
    ) {
        fun toOrderDeliveryInfo(): OrderDeliveryInfo {
            checkNotNull(method) { "method is null" }
            checkNotNull(method.method) { "method is null" }
            return OrderDeliveryInfo(
                method = method.method.toOrderDeliveryMethod(),
            )
        }

        @Serializable
        data class Method(
            @SerialName("type")
            val method: OrderDeliveryMethodDto? = null,

            @SerialName("price")
            val price: Long? = null,
        )
    }

    @Serializable
    data class PaymentMethod(
        @SerialName("code")
        val method: OrderPaymentMethodDto? = null,
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
        fun toOrderContactInfo(): OrderContactInfo {
            checkNotNull(firstName) { "firstName is null" }
            checkNotNull(email) { "email is null" }
            return OrderContactInfo(
                firstName = firstName,
                lastName = lastName,
                email = Email.create(email),
                phone = phone?.let { PhoneNumber.create(it) },
            )
        }
    }
}
