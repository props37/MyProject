package ru.livetyping.zarina.core.uicompose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductPrice
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import java.util.UUID

public class ProductShortPreviewParameterProvider : PreviewParameterProvider<ProductShort> {
    override val values: Sequence<ProductShort>
        get() = sequenceOf(provide())

    public companion object {
        public fun provide(
            id: Product.Id = Product.Id(UUID.randomUUID().toString()),
            name: String = "Худи на молнии с декоративными элементами",
            price: ProductPrice = ProductPricePreviewParameterProvider.provide(),
            offers: List<ProductOffer> = emptyList(),
            colors: List<ProductColor> = emptyList(),
            media: List<Media> = emptyList(),
            isInWishlist: Boolean = false,
            isInCart: Boolean = false,
        ): ProductShort {
            return ProductShort(
                id = id,
                name = name,
                price = price,
                offers = offers,
                colors = colors,
                media = media,
                isInWishlist = isInWishlist,
                isInCart = isInCart,
            )
        }
    }
}
