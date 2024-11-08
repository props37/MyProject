package ru.livetyping.zarina.presentation.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderDetails
import ru.livetyping.zarina.domain.order.OrderDetails.Product
import ru.livetyping.zarina.presentation.model.LocalDateParcelable
import ru.livetyping.zarina.presentation.model.product.PriceParcelable
import ru.livetyping.zarina.presentation.model.product.ProductColorParcelable
import ru.livetyping.zarina.domain.product.Product as DomainProduct

@Serializable
@Parcelize
data class OrderDetailsParcelable(
    val id: Long,
    val number: String,
    val productCount: Int,
    val date: LocalDateParcelable,
    val status: OrderStatusParcelable,
    val products: List<Product>,
    val price: OrderPriceParcelable,
    val paymentMethodType: PaymentMethodTypeParcelable,
    val paymentUrl: String?,
    val deliveryInfo: OrderDeliveryInfoParcelable,
    val contactInfo: OrderContactInfoParcelable,
    val deliveryAddress: String,
    val isCancellable: Boolean,
) : Parcelable {
    fun toOrderDetails(): OrderDetails {
        return OrderDetails(
            id = Order.Id(id),
            number = Order.Number(number),
            productCount = productCount,
            date = date.toLocalDate(),
            status = status.toOrderStatus(),
            products = products.map { it.toOrderDetailsProduct() },
            price = price.toOrderPrice(),
            paymentMethodType = paymentMethodType.toPaymentMethodType(),
            paymentUrl = paymentUrl?.let { Url(it) },
            deliveryInfo = deliveryInfo.toOrderDeliveryInfo(),
            contactInfo = contactInfo.toOrderContactInfo(),
            deliveryAddress = deliveryAddress,
            isCancellable = isCancellable,
        )
    }

    @Serializable
    @Parcelize
    data class Product(
        val id: String,
        val name: String,
        val size: String,
        val color: ProductColorParcelable,
        val imageUrl: String,
        val price: PriceParcelable,
        val count: Int,
    ) : Parcelable {
        fun toOrderDetailsProduct(): OrderDetails.Product {
            return Product(
                id = DomainProduct.Id(id),
                name = name,
                size = size,
                color = color.toProductColor(),
                imageUrl = Url(imageUrl),
                price = price.toPrice(),
                count = count,
            )
        }

        companion object {
            fun from(product: OrderDetails.Product): Product {
                return Product(
                    id = product.id.value,
                    name = product.name,
                    size = product.size,
                    color = ProductColorParcelable.from(product.color),
                    imageUrl = product.imageUrl.value,
                    price = PriceParcelable.from(product.price),
                    count = product.count,
                )
            }
        }
    }

    companion object {
        fun from(order: OrderDetails): OrderDetailsParcelable {
            return OrderDetailsParcelable(
                id = order.id.value,
                number = order.number.value,
                productCount = order.productCount,
                date = LocalDateParcelable.from(order.date),
                status = OrderStatusParcelable.from(order.status),
                products = order.products.map { Product.from(it) },
                price = OrderPriceParcelable.from(order.price),
                paymentMethodType = PaymentMethodTypeParcelable.from(order.paymentMethodType),
                paymentUrl = order.paymentUrl?.value,
                deliveryInfo = OrderDeliveryInfoParcelable.from(order.deliveryInfo),
                contactInfo = OrderContactInfoParcelable.from(order.contactInfo),
                deliveryAddress = order.deliveryAddress,
                isCancellable = order.isCancellable,
            )
        }
    }
}
