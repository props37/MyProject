package ru.livetyping.zarina.core.uimodel.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor

@Serializable
@Parcelize
public data class ProductColorParcelable(
    val id: String,
    val name: String,
    val color: String,
    val productId: String,
) : Parcelable {
    public fun toProductColor(): ProductColor {
        return ProductColor(
            id = ProductColor.Id(id),
            name = name,
            color = Color(color),
            productId = Product.Id(productId),
        )
    }

    public companion object {
        public fun from(productColor: ProductColor): ProductColorParcelable {
            return ProductColorParcelable(
                id = productColor.id.value,
                name = productColor.name,
                color = productColor.color.value,
                productId = productColor.productId.value,
            )
        }
    }
}
