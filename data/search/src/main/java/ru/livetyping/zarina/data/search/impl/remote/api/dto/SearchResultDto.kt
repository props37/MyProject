package ru.livetyping.zarina.data.search.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.PriceRange
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductPriceFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductColorFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSizeFilterItem
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductSortFilterItem
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.ProductShortDto

@Serializable
internal data class SearchResultDto(
    @SerialName("products")
    val products: List<ProductShortDto>? = null,

    @SerialName("totalHits")
    val totalHits: Int? = null,

    @SerialName("facets")
    val facets: List<Filter>? = null,

    @SerialName("pageCount")
    val pageCount: Int? = null,

    @SerialName("offset")
    val offset: Int? = null,
) {
    fun toSearchResult(): SearchResult {
        checkPropertyNotNull(products) { ::products }
        checkPropertyNotNull(totalHits) { ::totalHits }
        checkPropertyNotNull(offset) { ::offset }
        val products = products.mapNotNull { it.toProductShort() }
        return SearchResult(
            products = products,
            productTotalCount = totalHits,
            availableFilters = getFilters(),
            offset = offset,
        )
    }

    private fun getFilters(): ProductFilters {
        return ProductFilters(
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

    private fun getSortingFilter(): ProductListFilter<ProductSortFilterItem> {
        val items = listOf(
            ProductSorting.NEW,
            ProductSorting.POPULAR,
            ProductSorting.PRICE_LOW_TO_HIGH,
            ProductSorting.PRICE_HIGH_TO_LOW,
        ).map {
            ProductSortFilterItem.from(sorting = it, isSelected = false)
        }
        return ProductListFilter(
            items = items,
            isSingleSelection = true,
            type = ProductFilter.Type.SORTING,
        )
    }

    private fun getPriceFilter(): ProductPriceFilter? {
        val priceFilter = facets?.find { it.name == Filter.NAME_PRICE }
        val min = priceFilter?.values?.find { it.id == "min" }?.value
        val max = priceFilter?.values?.find { it.id == "max" }?.value
        return if (min != null && max != null) {
            return ProductPriceFilter(min = null, max = null, limits = PriceRange(min, max))
        } else null
    }

    private fun getSizeFilter(): ProductListFilter<ProductSizeFilterItem>? {
        val sizeFilter = facets?.find { it.name == Filter.NAME_SIZES }
        val items = sizeFilter?.values?.mapNotNull { value ->
            if (value.id != null && value.name != null) {
                ProductSizeFilterItem(
                    id = ProductListFilterItem.Id(value.id),
                    name = value.name,
                    isSelected = false,
                )
            } else null
        }
        return if (items != null) {
            ProductListFilter(
                items = items,
                isSingleSelection = false,
                type = ProductFilter.Type.SIZES,
            )
        } else null
    }

    private fun getColorFilter(): ProductListFilter<ProductColorFilterItem>? {
        val colorFilter = facets?.find { it.name == Filter.NAME_COLORS }
        val items = colorFilter?.values?.mapNotNull { value ->
            if (value.id != null && value.name != null) {
                ProductColorFilterItem(
                    id = ProductListFilterItem.Id(value.id),
                    name = value.name,
                    isSelected = false,
                    color = null,
                )
            } else null
        }
        return if (items != null) {
            ProductListFilter(
                items = items,
                isSingleSelection = false,
                type = ProductFilter.Type.COLORS,
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
