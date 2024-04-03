package ru.livetyping.zarina.data.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.PaginationInfo

@Serializable
data class PaginationInfoDto(
    @SerialName("current_page")
    val currentPage: Int? = null,

    @SerialName("total_pages")
    val pageCount: Int? = null,

    @SerialName("page_size")
    val pageSize: Int? = null,
) {
    fun toPaginationInfo(itemCount: Int): PaginationInfo = PaginationInfo(
        currentPage = checkNotNull(currentPage) { "currentPage is null" },
        pageCount = checkNotNull(pageCount) { "pageCount is null" },
        pageSize = checkNotNull(pageSize) { "pageSize is null" },
        itemCount = itemCount,
    )
}
