package ru.zarina.zarina.data.search.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.ListFilter
import ru.zarina.zarina.domain.PriceRange
import ru.zarina.zarina.domain.TreeFilter

@Serializable
data class FacetDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("values")
    val values: List<FacetValueDto>? = null,
) {

    private val valuesById by lazy { values?.associateBy { it.id }.orEmpty() }

    fun toPriceRange(): PriceRange? {
        val minValue = valuesById[ID_MIN]?.value
        val maxValue = valuesById[ID_MAX]?.value
        return if (
            ApiContract.isNotNull(minValue, ID_MIN)
            && ApiContract.isNotNull(maxValue, ID_MAX)
        )
            PriceRange(minValue.toInt(), maxValue.toInt())
        else
            null
    }

    fun toListFilter(): ListFilter? {
        val values = values?.mapNotNull { it.toListFilterItem() }
        return if (values.isNullOrEmpty()) {
            null
        } else {
            ListFilter(
                items = values,
                isSingleSelection = false
            )
        }
    }

    fun toTreeFilter(): TreeFilter? {
        val values = values?.mapNotNull { it.toTreeFilterItem() }
        return if (values.isNullOrEmpty()) {
            null
        } else {
            TreeFilter(
                items = values,
                isSingleSelection = false
            )
        }
    }

    companion object {
        private const val ID_MIN = "min"
        private const val ID_MAX = "max"

        const val NAME_PRICE = "price"
        const val NAME_CATEGORIES = "categories"
        const val NAME_SIZE = "Размер"
        const val NAME_COLOR = "Цвет"
    }

}
