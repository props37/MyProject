package ru.livetyping.zarina.data.old.favorites.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.product.remote.api.dto.PaginationDto
import ru.livetyping.zarina.data.old.remote.zarina.dto.ProductDto
import ru.livetyping.zarina.domain.old.Pagination

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
