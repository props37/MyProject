package ru.livetyping.zarina.feature.wishlist.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature.NavActions
import ru.livetyping.zarina.feature.wishlist.ui.WishlistFeature.NavEntry

public interface WishlistFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry {

        @Serializable
        public data object StartNavEntry : NavigationEntry
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onGoToCatalogClicked: () -> Unit,
        public val onProductClicked: (Product) -> Unit,
        public val onSubscribeToProductClicked: (Product, ProductOffer) -> Unit,
    ) : NavigationActions
}
