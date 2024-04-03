package ru.livetyping.zarina.data.old.content.remote.api.dto

import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.remote.zarina.dto.ProductDto
import ru.livetyping.zarina.domain.old.Selection

@Serializable
data class SelectionDto(
    @SerialName("title")
    val title: String? = null,
    @SerialName("subtitle")
    val subtitle: String? = null,
    @SerialName("banners")
    val banners: List<BannerDto>? = null,
    @SerialName("products")
    val products: List<ProductDto>? = null
) {
    fun toDomain(): List<Selection> {
        val banners = this.banners?.mapNotNull { it.toDomain() }.orEmpty()
        val products = this.products?.mapNotNull { it.toDomain() }.orEmpty()
        return buildList(2) {
            if (banners.isNotEmpty())
                add(
                    Selection.Banners(
                        banners = banners.toPersistentList(),
                    )
                )
            if (products.isNotEmpty())
                add(
                    Selection.Products(
                        title = title,
                        subtitle = subtitle,
                        products = products.toPersistentList(),
                    )
                )
        }
    }
}
