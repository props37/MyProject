package ru.livetyping.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.filter.Filter

@Serializable
@Parcelize
enum class FilterTypeParcelable : Parcelable {
    SORTING,
    PRICE,
    MATERIALS,
    SIZES,
    COLORS,
    DELIVERY_AVAILABILITY,
    STORE_PICKUP_AVAILABILITY;

    fun toFilterType(): Filter.Type = when (this) {
        SORTING -> Filter.Type.SORTING
        PRICE -> Filter.Type.PRICE
        MATERIALS -> Filter.Type.MATERIALS
        SIZES -> Filter.Type.SIZES
        COLORS -> Filter.Type.COLORS
        DELIVERY_AVAILABILITY -> Filter.Type.DELIVERY_AVAILABILITY
        STORE_PICKUP_AVAILABILITY -> Filter.Type.STORE_PICKUP_AVAILABILITY
    }

    companion object {
        fun from(type: Filter.Type): FilterTypeParcelable = when (type) {
            Filter.Type.SORTING -> SORTING
            Filter.Type.PRICE -> PRICE
            Filter.Type.MATERIALS -> MATERIALS
            Filter.Type.SIZES -> SIZES
            Filter.Type.COLORS -> COLORS
            Filter.Type.DELIVERY_AVAILABILITY -> DELIVERY_AVAILABILITY
            Filter.Type.STORE_PICKUP_AVAILABILITY -> STORE_PICKUP_AVAILABILITY
        }
    }
}
