package ru.livetyping.zarina.data.productsearch.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductItemDto
import ru.livetyping.zarina.domain.common.PriceRange
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.ColorFilterItem
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.ListFilterItem
import ru.livetyping.zarina.domain.filter.PriceFilter
import ru.livetyping.zarina.domain.filter.SizeFilterItem
import ru.livetyping.zarina.domain.filter.SortFilterItem
import ru.livetyping.zarina.domain.productsearch.ProductSearchResult
import ru.livetyping.zarina.domain.filter.Filter as DomainFilter

@Serializable
data class SearchProductsDto(
    @SerialName("products")
    val products: List<ProductItemDto>? = null,

    @SerialName("totalHits")
    val productTotalCount: Int? = null,

    @SerialName("facets")
    val filters: List<Filter>? = null,

    @SerialName("pageCount")
    val pageSize: Int? = null,

    @SerialName("offset")
    val offset: Int? = null,
) {
    fun toProductSearchResult(): ProductSearchResult {
        checkNotNull(products) { "products is null" }
        checkNotNull(productTotalCount) { "productTotalCount is null" }
        checkNotNull(offset) { "offset is null" }
        val products = products.mapNotNull { it.toProductItem() }
        return ProductSearchResult(
            products = products,
            productTotalCount = productTotalCount,
            filters = getFilters(),
            offset = offset,
        )
    }

    private fun getFilters(): Filters {
        return Filters(
            sorting = getSortingFilter(),
            price = getPriceFilter(),
            materials = null,
            sizes = getSizeFilter(),
            colors = getColorFilter(),
            deliveryAvailability = null,
            storePickupAvailability = null,
            pickupStores = null,
        )
    }

    private fun getSortingFilter(): ListFilter<SortFilterItem> {
        val items = listOf(
            Sorting.NEW,
            Sorting.POPULAR,
            Sorting.PRICE_LOW_TO_HIGH,
            Sorting.PRICE_HIGH_TO_LOW,
        ).map {
            SortFilterItem.from(sorting = it, isSelected = false)
        }
        return ListFilter(items = items, isSingleSelection = true, type = DomainFilter.Type.SORTING)
    }

    private fun getPriceFilter(): PriceFilter? {
        val priceFilter = filters?.find { it.name == Filter.NAME_PRICE }
        val min = priceFilter?.values?.find { it.id == "min" }?.value
        val max = priceFilter?.values?.find { it.id == "max" }?.value
        return if (min != null && max != null) {
            return PriceFilter(min = null, max = null, limits = PriceRange(min, max))
        } else null
    }

    private fun getSizeFilter(): ListFilter<SizeFilterItem>? {
        val sizeFilter = filters?.find { it.name == Filter.NAME_SIZES }
        val items = sizeFilter?.values?.mapNotNull { value ->
            if (value.id != null && value.name != null) {
                SizeFilterItem(
                    id = ListFilterItem.Id(value.id),
                    name = value.name,
                    isSelected = false,
                )
            } else null
        }
        return if (items != null) {
            ListFilter(
                items = items,
                isSingleSelection = false,
                type = DomainFilter.Type.SIZES,
            )
        } else null
    }

    private fun getColorFilter(): ListFilter<ColorFilterItem>? {
        val colorFilter = filters?.find { it.name == Filter.NAME_COLORS }
        val items = colorFilter?.values?.mapNotNull { value ->
            if (value.id != null && value.name != null) {
                ColorFilterItem(
                    id = ListFilterItem.Id(value.id),
                    name = value.name,
                    isSelected = false,
                    color = null,
                )
            } else null
        }
        return if (items != null) {
            ListFilter(
                items = items,
                isSingleSelection = false,
                type = DomainFilter.Type.COLORS,
            )
        } else null
    }

    @Serializable
    data class Filter(
        @SerialName("name")
        val name: String? = null,

        @SerialName("values")
        val values: List<Value>? = null,
    ) {
        @Serializable
        data class Value(
            @SerialName("id")
            val id: String? = null,

            @SerialName("name")
            val name: String? = null,

            @SerialName("value")
            val value: Int? = null,
        )

        companion object {
            const val NAME_PRICE = "price"
            const val NAME_SIZES = "Размер"
            const val NAME_COLORS = "Цвет"
        }
    }
}
