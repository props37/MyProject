package ru.livetyping.zarina.feature.wishlist.ui.impl.screen

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface WishlistScreenAction {
    data object BackClicked : WishlistScreenAction

    data object GoToCatalogClicked : WishlistScreenAction

    data class ProductClicked(val product: Product) : WishlistScreenAction

    data class CategoryShortcutClicked(val categoryId: Category.Id) : WishlistScreenAction

    data object SearchClicked : WishlistScreenAction
}
