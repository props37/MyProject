package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.media.Media

// Marked as stable on config/compose/stability_config.txt
public data class ProductDetailed(
    override val id: Id,
    override val name: String,
    override val price: ProductPrice,
    override val offers: List<ProductOffer>,
    override val colors: List<ProductColor>,
    override val media: List<Media>,
    override val isInWishlist: Boolean,
    override val isInCart: Boolean,
    val label: Label?,
    val description: List<DescriptionEntry>,
    val bonusAccrualForPurchase: Int,
    val freeDeliveryTotalPriceThreshold: Int,
    val shareUrl: Url?,
    val modelInfo: ModelInfo?,
) : Product() {
    public val podeliPrice: PodeliPrice by lazy {
        PodeliPrice.create(price.currentPrice)
    }

    // Marked as stable on config/compose/stability_config.txt
    public data class Label(
        val name: String,
        val color: Color?,
    )

    // Marked as stable on config/compose/stability_config.txt
    public data class DescriptionEntry(
        val title: String,
        val body: String,
    )

    // Marked as stable on config/compose/stability_config.txt
    public data class ModelInfo(
        val modelParams: String?,
        val sizeOnModel: String?,
    ) {
        public fun isEmpty(): Boolean = modelParams == null && sizeOnModel == null
    }
}
