package ru.livetyping.zarina.core.uicompose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import java.math.BigDecimal

public class ProductPricePreviewParameterProvider : PreviewParameterProvider<ProductPrice> {
    override val values: Sequence<ProductPrice>
        get() = sequenceOf(
            provide(
                originalPrice = BigDecimal("2999"),
                discount = ProductPrice.Discount(
                    discountPrice = BigDecimal("2249"),
                    discountPercent = BigDecimal("25"),
                ),
            ),
            provide()
        )

    public companion object {
        public fun provide(
            originalPrice: BigDecimal = BigDecimal("1999"),
            discount: ProductPrice.Discount? = null,
        ): ProductPrice {
            return ProductPrice(originalPrice, discount)
        }
    }
}
