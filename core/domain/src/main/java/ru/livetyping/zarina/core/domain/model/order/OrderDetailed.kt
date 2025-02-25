package ru.livetyping.zarina.core.domain.model.order

import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethodType
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import java.time.LocalDate
import ru.livetyping.zarina.core.domain.model.product.Product as ProductDomain

// Marked as stable on config/compose/stability_config.txt
public data class OrderDetailed(
    override val id: Id,
    override val number: Number,
    override val productCount: Int,
    override val date: LocalDate,
    override val status: OrderStatus,
    val products: List<Product>,
    val price: OrderPrice,
    val paymentMethodType: PaymentMethodType,
    val paymentUrl: Url?,
    val deliveryInfo: OrderDeliveryInfo,
    val recipient: OrderRecipient,
    val deliveryAddress: String?,
    val isCancellable: Boolean,
) : Order() {
    override val totalPrice: Int
        get() = price.totalPrice

    // Marked as stable on config/compose/stability_config.txt
    public data class Product(
        val id: Id,
        val productId: ProductDomain.Id,
        val name: String,
        val size: String,
        val color: ProductColor,
        val imageUrl: Url,
        val price: ProductPrice,
        val count: Int,
    ) {
        @JvmInline
        public value class Id(public val value: String)
    }
}
