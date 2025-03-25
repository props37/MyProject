package ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.uimodel.product.filter.ProductListFilterParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
internal class ListFilterNavEntry private constructor(
    val listFilter: ProductListFilterParcelable,
) : NavigationEntry {
    companion object {
        fun create(listFilter: ProductListFilter<*>): ListFilterNavEntry {
            val listFilterParcelable = ProductListFilterParcelable.from(listFilter)
            return ListFilterNavEntry(listFilterParcelable)
        }

        fun typeMap(): Map<KType, NavType<*>> {
            val listFilterType = parcelableNavType<ProductListFilterParcelable>(
                isNullableAllowed = false,
            )
            return mapOf(typeOf<ProductListFilterParcelable>() to listFilterType)
        }
    }
}
