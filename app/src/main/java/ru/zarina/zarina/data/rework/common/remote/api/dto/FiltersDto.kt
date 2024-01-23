package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Color
import ru.zarina.zarina.domain.rework.filter.ColorFilterItem
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem
import ru.zarina.zarina.domain.rework.filter.MaterialFilterItem
import ru.zarina.zarina.domain.rework.filter.PriceFilter
import ru.zarina.zarina.domain.rework.filter.SizeFilterItem

@Serializable
data class FiltersDto(
    @SerialName("price")
    val price: Price? = null,

    @SerialName("materials")
    val materials: List<BasicItem>? = null,

    @SerialName("sizes")
    val sizes: List<BasicItem>? = null,

    @SerialName("colors")
    val colors: List<ColorItem>? = null,
) {
    fun toFilters(): Filters {
        checkNotNull(price) { "price is null" }
        checkNotNull(materials) { "materials is null" }
        checkNotNull(sizes) { "sizes is null" }
        checkNotNull(colors) { "colors is null" }
        val materials = ListFilter(
            items = materials.map { it.toMaterialFilterItem() },
            isSingleSelection = false,
        )
        val sizes = ListFilter(
            items = sizes.map { it.toSizeFilterItem() },
            isSingleSelection = false,
        )
        val colors = ListFilter(
            items = colors.map { it.toColorFilterItem() },
            isSingleSelection = false,
        )
        return Filters(
            sorting = null, // TODO: [High] Figure out what to do with sorting
            price = price.toPriceFilter(),
            materials = materials,
            sizes = sizes,
            colors = colors,
        )
    }

    @Serializable
    data class Price(
        @SerialName("min")
        val min: Long? = null,

        @SerialName("max")
        val max: Long? = null,
    ) {
        fun toPriceFilter(): PriceFilter = PriceFilter(
            min = checkNotNull(min) { "min is null" },
            max = checkNotNull(max) { "max is null" },
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
