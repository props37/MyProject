package ru.livetyping.zarina.data.search.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

@Serializable
internal data class SearchRequestBody(
    @SerialName("query")
    val query: String,

    @SerialName("sort")
    val sorting: SearchSortingDto,

    @SerialName("filter")
    val filters: Filters?,

    @SerialName("offset")
    val offset: Int,
) {
    @Serializable
    data class Filters(
        @SerialName("price")
        val price: String?,

        @SerialName("Размер")
        val sizes: String?,

        @SerialName("Цвет")
        val colors: String?,
    ) {
        companion object {
            fun from(filters: ProductFilters): Filters? {
                if (!filters.hasAppliedIgnoringSorting) return null
                val price = filters.price?.let {
                    val min = it.min ?: it.limits.min
                    val max = it.max ?: it.limits.max
                    "$min$SEPARATOR$max"
                }
                val sizes = filters.sizes?.let { listFilter ->
                    listFilter.selectedItems.joinToString(SEPARATOR) { it.name }
                }
                val colors = filters.colors?.let { listFilter ->
                    listFilter.selectedItems.joinToString(SEPARATOR) { it.name }
                }
                return Filters(
                    price = price,
                    sizes = sizes,
                    colors = colors,
                )
            }

            private const val SEPARATOR = ";"
        }
    }
}
