package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.pagination.PaginationInfo
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

@Serializable
public data class PaginationInfoDto(
    @SerialName("current_page")
    val currentPage: Int? = null,

    @SerialName("total_pages")
    val totalPages: Int? = null,

    @SerialName("page_size")
    val pageSize: Int? = null,
) {
    public fun toPaginationInfo(itemTotalCount: Int): PaginationInfo {
        return PaginationInfo(
            currentPage = checkPropertyNotNull(currentPage) { ::currentPage },
            pageCount = checkPropertyNotNull(totalPages) { ::totalPages },
            pageSize = checkPropertyNotNull(pageSize) { ::pageSize },
            itemTotalCount = itemTotalCount,
        )
    }
}
