package ru.livetyping.zarina.presentation.model.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Color
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductColor

@Serializable
@Parcelize
data class ProductColorParcelable(
    val id: String,
    val name: String,
    val color: String,
    val productId: String,
) : Parcelable {
    fun toProductColor(): ProductColor = ProductColor(
        id = ProductColor.Id(id),
        name = name,
        color = Color(color),
        productId = Product.Id(productId),
    )

    companion object {
        fun from(productColor: ProductColor): ProductColorParcelable = ProductColorParcelable(
            id = productColor.id.value,
            name = productColor.name,
            color = productColor.color.value,
            productId = productColor.productId.value,
        )
    }
}
