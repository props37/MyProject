package ru.livetyping.zarina.presentation.screen.productsearch

import ru.livetyping.zarina.presentation.screen.productsearch.paging.ProductSearchResultPager
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.GetFavoriteProductIdsFlowUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.usecase.productsearch.ClearProductSearchHistoryUseCase
import ru.livetyping.zarina.usecase.productsearch.DeleteProductSearchHistoryQueryUseCase
import ru.livetyping.zarina.usecase.productsearch.GetLastProductSearchHistoryQueriesFlowUseCase
import ru.livetyping.zarina.usecase.productsearch.GetProductSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.usecase.productsearch.SaveProductSearchHistoryQueryUseCase
import javax.inject.Inject

class ProductSearchInteractor @Inject constructor(
    val getProductSearchSuggestionsFlow: GetProductSearchSuggestionsFlowUseCase,
    val productSearchResultPager: ProductSearchResultPager,
    val getFavoriteProductIdsFlow: GetFavoriteProductIdsFlowUseCase,
    val getCardProductsIdsFlow: GetCartProductIdsFlowUseCase,
    val toggleProductPresenceInFavorites: ToggleProductPresenceInFavoritesUseCase,
    val addProductToCart: AddProductToCartUseCase,
    val getLastProductSearchHistoryQueriesFlow: GetLastProductSearchHistoryQueriesFlowUseCase,
    val saveProductSearchHistoryQuery: SaveProductSearchHistoryQueryUseCase,
    val deleteProductSearchHistoryQuery: DeleteProductSearchHistoryQueryUseCase,
    val clearProductSearchHistory: ClearProductSearchHistoryUseCase,
)
