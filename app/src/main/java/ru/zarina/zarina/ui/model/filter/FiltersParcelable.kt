package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.filter.Filters

@Serializable
@Parcelize
data class FiltersParcelable(
    val sorting: ListFilterParcelable?,
    val price: PriceFilterParcelable?,
    val materials: ListFilterParcelable?,
    val sizes: ListFilterParcelable?,
    val colors: ListFilterParcelable?,
) : Parcelable {
    companion object {
        fun from(filters: Filters): FiltersParcelable {
            return FiltersParcelable(
                sorting = filters.sorting?.let { ListFilterParcelable.from(it) },
                price = filters.price?.let { PriceFilterParcelable.from(it) },
                materials = filters.materials?.let { ListFilterParcelable.from(it) },
                sizes = filters.sizes?.let { ListFilterParcelable.from(it) },
                colors = filters.colors?.let { ListFilterParcelable.from(it) },
            )
        }
    }
}
