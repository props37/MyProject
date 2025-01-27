package ru.livetyping.zarina.feature.product.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature.NavActions
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature.NavEntry

public interface ProductFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry private constructor(private val productId: String) : NavigationEntry {
        public fun getProductId(): Product.Id = Product.Id(productId)

        public companion object {
            public fun create(productId: Product.Id): NavEntry = NavEntry(productId.value)
        }
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
    ) : NavigationActions
}
