package ru.livetyping.zarina.feature.search.ui.impl.impl.filtration

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class FiltrationNavEntry private constructor(
    val searchQuery: String,
    val filters: ProductFiltersParcelable?,
) : NavigationEntry {
    companion object {
        fun create(searchQuery: String, filters: ProductFilters?): FiltrationNavEntry {
            return FiltrationNavEntry(
                searchQuery = searchQuery,
                filters = filters?.let { ProductFiltersParcelable.from(it) },
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val filtersType = ParcelableNavType<ProductFiltersParcelable?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            )
            return mapOf(typeOf<ProductFiltersParcelable?>() to filtersType)
        }
    }
}
