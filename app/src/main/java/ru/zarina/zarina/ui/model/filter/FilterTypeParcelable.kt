package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.filter.Filter

@Serializable
@Parcelize
enum class FilterTypeParcelable : Parcelable {
    SORTING,
    PRICE,
    MATERIALS,
    SIZES,
    COLORS;

    fun toFilterType(): Filter.Type = when (this) {
        SORTING -> Filter.Type.SORTING
        PRICE -> Filter.Type.PRICE
        MATERIALS -> Filter.Type.MATERIALS
        SIZES -> Filter.Type.SIZES
        COLORS -> Filter.Type.COLORS
    }

    companion object {
        fun from(type: Filter.Type): FilterTypeParcelable = when (type) {
            Filter.Type.SORTING -> SORTING
            Filter.Type.PRICE -> PRICE
            Filter.Type.MATERIALS -> MATERIALS
            Filter.Type.SIZES -> SIZES
            Filter.Type.COLORS -> COLORS
        }
    }
}
