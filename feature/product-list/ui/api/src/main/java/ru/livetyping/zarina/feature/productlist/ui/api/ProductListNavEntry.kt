package ru.livetyping.zarina.feature.productlist.ui.api

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Serializable
public data class ProductListNavEntry(
    val categoryId: String,
    val filters: ProductFiltersParcelable? = null,
) : NavigationEntry {
    public companion object {
        public const val CATEGORY_ID_PROPERTY_NAME: String = "categoryId"

        public fun typeMap(): Map<KType, NavType<*>> {
            val filtersType = object : ParcelableNavType<ProductFiltersParcelable?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            ) {}
            return mapOf(
                typeOf<ProductFiltersParcelable?>() to filtersType,
            )
        }
    }
}
