package ru.livetyping.zarina.feature.wishlist.ui.impl.screen.model

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface WishlistEvent {
    data object BackClicked : WishlistEvent

    data class ProductClicked(val product: Product) : WishlistEvent

    data class AddProductToWishlistClicked(val product: Product) : WishlistEvent

    data object SearchClicked : WishlistEvent

    data class CategoryShortcutClicked(val categoryId: Category.Id) : WishlistEvent

    data object RefreshTriggered : WishlistEvent

    data class ProductAppendError(val throwable: Throwable) : WishlistEvent
}
