package ru.livetyping.zarina.presentation.screen.productsearch

import ru.livetyping.zarina.presentation.screen.productsearch.paging.ProductSearchResultPager
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.productsearch.GetLastProductSearchHistoryEntriesFlowUseCase
import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.usecase.productsearch.SaveProductSearchHistoryEntryUseCase
import javax.inject.Inject

class ProductSearchInteractor @Inject constructor(
    val getProductSearchSuggestionsFlow: GetProductSearchSuggestionsFlowUseCase,
    val productSearchResultPager: ProductSearchResultPager,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val getCardProductsIdsFlow: GetCartProductIdsFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val getLastProductSearchHistoryEntriesFlow: GetLastProductSearchHistoryEntriesFlowUseCase,
    val saveProductSearchHistoryEntryFlow: SaveProductSearchHistoryEntryUseCase,
)
