package ru.livetyping.zarina.core.uicompose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import java.math.BigDecimal

public class ProductPricePreviewParameterProvider : PreviewParameterProvider<ProductPrice> {
    override val values: Sequence<ProductPrice>
        get() = sequenceOf(
            provide(
                originalPrice = BigDecimal("2999"),
                discountPrice = BigDecimal("2999"),
            ),
            provide()
        )

    public companion object {
        public fun provide(
            originalPrice: BigDecimal = BigDecimal("1999"),
            discountPrice: BigDecimal = BigDecimal("1499"),
            discountPercent: BigDecimal = BigDecimal(25),
        ): ProductPrice {
            return ProductPrice(
                originalPrice = originalPrice,
                hasDiscount = discountPrice != originalPrice,
                discountPrice = discountPrice,
                discountPercent = discountPercent,
            )
        }
    }
}
