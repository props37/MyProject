package ru.livetyping.zarina.feature.productlist.ui.impl.impl.filtration

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class FiltrationNavEntry private constructor(
    private val categoryId: String,
    val filters: ProductFiltersParcelable?,
) : NavigationEntry {
    fun getCategoryId(): Category.Id = Category.Id(categoryId)

    companion object {
        fun create(categoryId: Category.Id, filters: ProductFilters?): FiltrationNavEntry {
            return FiltrationNavEntry(
                categoryId = categoryId.value,
                filters = filters?.let { ProductFiltersParcelable.from(it) },
            )
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val filtersType = parcelableNavType<ProductFiltersParcelable?>(
                isNullableAllowed = true,
            )
            return mapOf(typeOf<ProductFiltersParcelable?>() to filtersType)
        }
    }
}
