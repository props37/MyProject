package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Pagination
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

    fun toDomain(pageIndex: Int, pageSize: Int): Page<FilteredProducts> {
        checkNotNull(totalHits)
        val pagination = Pagination(
            currentPageIndex = pageIndex,
            pageCount = ceil(totalHits.toDouble() / pageSize).toInt(),
            totalItemCount = totalHits
        )

        val filtration = facets?.toDomain() ?: Filtration.EMPTY
        val products = products?.mapNotNull { it.toDomain() }.orEmpty()

        return Page(
            pagination = pagination,
            value = FilteredProducts(
                filtration = filtration,
                products = products
            )
        )
    }

}

fun List<FacetDto>.toDomain(): Filtration {
    val facetsByName = this.associateBy { it.name }
    val price = facetsByName[FacetDto.NAME_PRICE]?.toPriceRange()
    val sizes = facetsByName[FacetDto.NAME_SIZE]?.toListFilter()
    val colors = facetsByName[FacetDto.NAME_COLOR]?.toListFilter()

    return Filtration(
        priceLimits = price,
        categories = null,
        colors = colors,
        attributes = null,
        materials = null,
        sizes = sizes,
        isShippingAvailable = null,
        isPickupAvailable = null,
        pickupShop = null,
    )
}
