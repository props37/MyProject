package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Pagination
import ru.zarina.zarina.domain.Product
import kotlin.math.ceil

@Serializable
data class SearchResultDto(
    @SerialName("totalHits")
    val totalHits: Int? = null,
    @SerialName("products")
    val products: List<ProductDto>? = null,
    @SerialName("facets")
    val facets: List<FacetDto>? = null,
) {

    fun toDomain(pageIndex: Int, pageSize: Int): Page<List<Product>> {
        checkNotNull(totalHits)
        val pagination = Pagination(
            currentPageIndex = pageIndex,
            pageCount = ceil(totalHits.toDouble() / pageSize).toInt(),
            totalItemCount = totalHits
        )

        val filtration = facets?.toDomain()

        return Page(
            pagination = pagination,
            value = this.products?.mapNotNull { it.toDomain() }.orEmpty()
        )
    }

}

fun List<FacetDto>.toDomain(): Filtration? {
    val facetsByName = this.associateBy { it.name }
    val price = facetsByName[FacetDto.NAME_PRICE]?.toPriceRange()

    return if (
        ApiContract.isNotNull(price, FacetDto.NAME_PRICE)
    ) {
        Filtration(
            priceLimits = price,
            categories = null,
            colors = null,
            attributes = null,
            materials = null,
            sizes = null,
            isShippingAvailable = null,
            isPickupAvailable = null,
            pickupShop = null,
        )
    } else {
        null
    }
}
