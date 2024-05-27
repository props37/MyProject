package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.SortingDto

@Serializable
data class GetProductsRequestBody(
    @SerialName("category_id")
    val categoryId: Long,

    @SerialName("filters")
    val filters: FiltersRequestDto?,

    @SerialName("sort")
    val sorting: SortingDto,

    @SerialName("page")
    val page: Int,

    @Transient
    val returnProducts: Boolean = true,
) {
    @Suppress("unused")
    @SerialName("count")
    val returnProductTotalCount: Boolean? = if (!returnProducts) true else null

    @Suppress("unused")
    @SerialName("filterRanges")
    val returnAvailableFilters: Boolean? = if (!returnProducts) true else null
}
