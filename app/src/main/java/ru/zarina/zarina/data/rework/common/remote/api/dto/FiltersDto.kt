package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Color
import ru.zarina.zarina.domain.rework.filter.ColorFilterItem
import ru.zarina.zarina.domain.rework.filter.Filter
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem
import ru.zarina.zarina.domain.rework.filter.MaterialFilterItem
import ru.zarina.zarina.domain.rework.filter.PriceFilter
import ru.zarina.zarina.domain.rework.filter.SizeFilterItem

@Serializable
data class FiltersDto(
    @SerialName("price")
    val price: PriceFilterDto? = null,

    @SerialName("materials")
    val materials: List<BasicItem>? = null,

    @SerialName("sizes")
    val sizes: List<BasicItem>? = null,

    @SerialName("colors")
    val colors: List<ColorItem>? = null,
) {
    fun toFilters(): Filters {
        checkNotNull(price) { "price is null" }
        val price = PriceFilter(min = null, max = null, limits = price.toPriceRange())
        val materials = if (!materials.isNullOrEmpty()) {
            ListFilter(
                items = materials.map { it.toMaterialFilterItem() },
                isSingleSelection = false,
                type = Filter.Type.MATERIALS,
            )
        } else null
        val sizes = if (!sizes.isNullOrEmpty()) {
            ListFilter(
                items = sizes.map { it.toSizeFilterItem() },
                isSingleSelection = false,
                type = Filter.Type.SIZES,
            )
        } else null
        val colors = if (!colors.isNullOrEmpty()) {
            ListFilter(
                items = colors.map { it.toColorFilterItem() },
                isSingleSelection = false,
                type = Filter.Type.COLORS,
            )
        } else null
        return Filters(
            sorting = Filters.getDefaultSorting(),
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
        )
    }

    @Serializable
    data class BasicItem(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("is_applied")
        val isApplied: Boolean? = null,
    ) {
        fun toMaterialFilterItem(): MaterialFilterItem {
            checkNotNull(id) { "id is null" }
            return MaterialFilterItem(
                id = checkNotNull(ListFilterItem.Id(id)),
                name = checkNotNull(name) { "name is null" },
                isSelected = checkNotNull(isApplied) { "isApplied is null" },
            )
        }

        fun toSizeFilterItem(): SizeFilterItem {
            checkNotNull(id) { "id is null" }
            return SizeFilterItem(
                id = checkNotNull(ListFilterItem.Id(id)),
                name = checkNotNull(name) { "name is null" },
                isSelected = checkNotNull(isApplied) { "isApplied is null" },
            )
        }
    }

    @Serializable
    data class ColorItem(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("code")
        val code: String? = null,

        @SerialName("is_applied")
        val isApplied: Boolean? = null,
    ) {
        fun toColorFilterItem(): ColorFilterItem {
            checkNotNull(id) { "id is null" }
            checkNotNull(code) { "code is null" }
            return ColorFilterItem(
                id = ListFilterItem.Id(id),
                name = checkNotNull(name) { "name is null" },
                isSelected = checkNotNull(isApplied) { "isApplied is null" },
                color = Color(code),
            )
        }
    }
}
