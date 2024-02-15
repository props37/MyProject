package ru.zarina.zarina.data.rework.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.zarina.zarina.data.rework.common.remote.api.dto.SortingDto

@Serializable
data class GetProductsBodyDto(
    @SerialName("category_id")
    val categoryId: Long,

    @SerialName("filters")
    val filters: FiltersBodyDto?,

    @SerialName("sort")
    val sorting: SortingDto,

    @SerialName("page")
    val page: Int,

    @Transient
    val returnProducts: Boolean = true,
) {
    @Suppress("unused")
    @SerialName("count")
    val itemCount: Boolean? = if (!returnProducts) true else null

    @Suppress("unused")
    @SerialName("filterRanges")
    val filterRanges: Boolean? = if (!returnProducts) true else null
}
