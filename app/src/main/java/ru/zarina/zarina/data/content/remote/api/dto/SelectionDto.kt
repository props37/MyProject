package ru.zarina.zarina.data.content.remote.api.dto

import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductDto
import ru.zarina.zarina.domain.Selection

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
    fun toDomain(): Selection? {
        if (!ApiContract.isNotNull(title, "title")) return null
        val banners = this.banners?.mapNotNull { it.toDomain() }.orEmpty()
        val products = this.products?.mapNotNull { it.toDomain() }.orEmpty()
        if (banners.isNotEmpty() || products.isNotEmpty())
            return Selection(
                title = title,
                subtitle = subtitle,
                banners = banners.toPersistentList(),
                products = products.toPersistentList()
            )
        else
            return null
    }
}
