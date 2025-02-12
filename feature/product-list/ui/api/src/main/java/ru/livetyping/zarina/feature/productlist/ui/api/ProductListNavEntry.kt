package ru.livetyping.zarina.feature.productlist.ui.api

import androidx.navigation.NavType
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public abstract class ProductListNavEntry : NavigationEntry {
    protected abstract val categoryId: String
    public abstract val filters: ProductFiltersParcelable?

    public fun getCategoryId(): Category.Id = Category.Id(categoryId)

    public companion object {
        public const val CATEGORY_ID_PROPERTY_NAME: String = "categoryId"

        public fun typeMap(): Map<KType, NavType<*>> {
            val filtersType = ParcelableNavType<ProductFiltersParcelable?>(
                isNullableAllowed = true,
                serializer = kotlinx.serialization.serializer(),
            )
            return mapOf(typeOf<ProductFiltersParcelable?>() to filtersType)
        }
    }
}
