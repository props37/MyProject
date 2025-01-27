package ru.livetyping.zarina.feature.productlist.ui.api

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature.NavActions
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature.NavEntry
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public interface ProductListFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry private constructor(
        private val categoryId: String,
        public val filters: ProductFiltersParcelable?,
    ) : NavigationEntry {
        public fun getCategoryId(): Category.Id = Category.Id(categoryId)

        public companion object {
            public const val CATEGORY_ID_PROPERTY_NAME: String = "categoryId"

            public fun create(
                categoryId: Category.Id,
                filters: ProductFilters? = null,
            ): NavEntry {
                return NavEntry(
                    categoryId = categoryId.value,
                    filters = filters?.let { ProductFiltersParcelable.from(it) },
                )
            }

            public fun typeMap(): Map<KType, NavType<*>> {
                val filtersType = ParcelableNavType<ProductFiltersParcelable?>(
                    isNullableAllowed = true,
                    serializer = kotlinx.serialization.serializer(),
                )
                return mapOf(typeOf<ProductFiltersParcelable?>() to filtersType)
            }
        }
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onTagClicked: (Category, ProductFilters?) -> Unit,
        public val onProductClicked: (Product) -> Unit,
        public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
    ) : NavigationActions
}
