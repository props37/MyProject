package ru.livetyping.zarina.feature.product.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature.NavActions
import ru.livetyping.zarina.feature.product.ui.api.ProductFeature.NavEntry

public interface ProductFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry private constructor(
        override val productId: String,
    ) : ProductNavEntry() {

        @Serializable
        public class StartNavEntry private constructor(
            override val productId: String,
        ) : ProductNavEntry()

        public companion object {
            public fun create(productId: Product.Id): NavEntry = NavEntry(productId.value)
        }
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
        public val onProductClicked: (Product) -> Unit,
    ) : NavigationActions
}
