package ru.livetyping.zarina.feature.productlist.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature.NavActions
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListFeature.NavEntry

public interface ProductListFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry private constructor(
        override val categoryId: String,
        override val filters: ProductFiltersParcelable?,
    ) : ProductListNavEntry() {
        public companion object {
            public fun create(
                categoryId: Category.Id,
                filters: ProductFilters? = null,
            ): NavEntry {
                return NavEntry(
                    categoryId = categoryId.value,
                    filters = filters?.let { ProductFiltersParcelable.from(it) },
                )
            }
        }
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onSearchClicked: () -> Unit,
        public val onProductClicked: (Product) -> Unit,
        public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
    ) : NavigationActions
}
