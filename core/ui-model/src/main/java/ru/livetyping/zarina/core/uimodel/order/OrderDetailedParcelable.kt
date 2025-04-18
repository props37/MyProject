package ru.livetyping.zarina.core.uimodel.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.order.Order
import ru.livetyping.zarina.core.domain.model.order.OrderDetailed
import ru.livetyping.zarina.core.uimodel.checkout.PaymentMethodTypeParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductColorParcelable
import ru.livetyping.zarina.core.uimodel.product.ProductPriceParcelable
import java.time.LocalDate
import ru.livetyping.zarina.core.domain.model.product.Product as DomainProduct

@Serializable
@Parcelize
public data class OrderDetailedParcelable(
    val id: String,
    val number: String,
    val productCount: Int,
    val date: String,
    val status: OrderStatusParcelable,
    val products: List<Product>,
    val price: OrderPriceParcelable,
    val paymentMethodType: PaymentMethodTypeParcelable,
    val paymentUrl: String?,
    val deliveryInfo: OrderDeliveryInfoParcelable,
    val recipient: OrderRecipientParcelable,
    val deliveryAddress: String?,
    val isCancellable: Boolean,
) : Parcelable {
    public fun toOrderDetailed(): OrderDetailed {
        return OrderDetailed(
            id = Order.Id(id),
            number = Order.Number(number),
            productCount = productCount,
            date = LocalDate.parse(date),
            status = status.toOrderStatus(),
            products = products.map { it.toProduct() },
            price = price.toOrderPrice(),
            paymentMethodType = paymentMethodType.toPaymentMethodType(),
            paymentUrl = paymentUrl?.let { Url.create(it) },
            deliveryInfo = deliveryInfo.toOrderDeliveryInfo(),
            recipient = recipient.toOrderRecipient(),
            deliveryAddress = deliveryAddress,
            isCancellable = isCancellable,
        )
    }

    @Serializable
    @Parcelize
    public data class Product(
        val id: String,
        val productId: String,
        val name: String,
        val size: String,
        val color: ProductColorParcelable,
        val imageUrl: String,
        val price: ProductPriceParcelable,
        val count: Int,
    ) : Parcelable {
        public fun toProduct(): OrderDetailed.Product {
            return OrderDetailed.Product(
                id = OrderDetailed.Product.Id(id),
                productId = DomainProduct.Id(productId),
                name = name,
                size = size,
                color = color.toProductColor(),
                imageUrl = Url.create(imageUrl),
                price = price.toProductPrice(),
                count = count,
            )
        }

        public companion object {
            public fun from(product: OrderDetailed.Product): Product {
                return Product(
                    id = product.id.value,
                    productId = product.productId.value,
                    name = product.name,
                    size = product.size,
                    color = ProductColorParcelable.from(product.color),
                    imageUrl = product.imageUrl.value,
                    price = ProductPriceParcelable.from(product.price),
                    count = product.count,
                )
            }
        }
    }

    public companion object {
        public fun from(order: OrderDetailed): OrderDetailedParcelable {
            return OrderDetailedParcelable(
                id = order.id.value,
                number = order.number.value,
                productCount = order.productCount,
                date = order.date.toString(),
                status = OrderStatusParcelable.from(order.status),
                products = order.products.map { Product.from(it) },
                price = OrderPriceParcelable.from(order.price),
                paymentMethodType = PaymentMethodTypeParcelable.from(order.paymentMethodType),
                paymentUrl = order.paymentUrl?.value,
                deliveryInfo = OrderDeliveryInfoParcelable.from(order.deliveryInfo),
                recipient = OrderRecipientParcelable.from(order.recipient),
                deliveryAddress = order.deliveryAddress,
                isCancellable = order.isCancellable,
            )
        }
    }
}
