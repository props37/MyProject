package ru.zarina.zarina.data.old.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    @SerialName("current_page")
    val currentPage: Int? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("page_size")
    val pageSize: Int? = null,
)
