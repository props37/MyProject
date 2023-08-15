package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Pagination
import ru.zarina.zarina.domain.Product
import kotlin.math.ceil

@Serializable
data class SearchResultDto(
    @SerialName("totalHits")
    val totalHits: Int? = null,
    val products: List<ProductDto>? = null,
    // TODO filters/facets
) {

    fun toDomain(pageIndex: Int, pageSize: Int): Page<List<Product>> {
        checkNotNull(totalHits)
        val pagination = Pagination(
            currentPageIndex = pageIndex,
            pageCount = ceil(totalHits.toDouble() / pageSize).toInt(),
            totalItemCount = totalHits
        )
        return Page(
            pagination = pagination,
            value = this.products?.mapNotNull { it.toDomain() }.orEmpty()
        )
    }

}
