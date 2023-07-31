package ru.zarina.zarina.data.favorites.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductDto
import ru.zarina.zarina.data.product.remote.api.dto.PaginationDto
import ru.zarina.zarina.domain.Pagination

@Serializable
data class FavoritesPageDto(
    @SerialName("items_count")
    val itemCount: Int? = null,
    @SerialName("items")
    val items: List<ProductDto>? = null,
    @SerialName("pagination")
    val pagination: PaginationDto? = null,
) {

    fun toPagination(): Pagination {
        checkNotNull(itemCount)
        checkNotNull(pagination)
        checkNotNull(pagination.currentPage)
        checkNotNull(pagination.totalPages)
        return Pagination(
            // adjust page index, because it starts from 1 on the backend
            currentPageIndex = pagination.currentPage - 1,
            pageCount = pagination.totalPages,
            totalItemCount = itemCount,
        )
    }

}
