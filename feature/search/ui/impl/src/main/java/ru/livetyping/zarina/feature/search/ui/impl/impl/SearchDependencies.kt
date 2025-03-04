package ru.livetyping.zarina.feature.search.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.ClearSearchHistoryUseCase
import ru.livetyping.zarina.core.domain.usecase.search.DeleteSearchHistoryQueryUseCase
import ru.livetyping.zarina.core.domain.usecase.search.GetLastSearchHistoryQueriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.GetSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.SaveSearchHistoryQueryUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.feature.search.ui.impl.impl.paging.SearchResultPager
import javax.inject.Inject

internal class SearchDependencies @Inject constructor(
    val searchResultPager: SearchResultPager,
    val getSearchSuggestionsFlow: GetSearchSuggestionsFlowUseCase,
    val getLastSearchHistoryQueriesFlow: GetLastSearchHistoryQueriesFlowUseCase,
    val saveSearchHistoryQuery: SaveSearchHistoryQueryUseCase,
    val deleteSearchHistoryQuery: DeleteSearchHistoryQueryUseCase,
    val clearSearchHistory: ClearSearchHistoryUseCase,
    val getWishlistProductIdsFlow: GetWishlistProductIdsFlowUseCase,
    val getCartProductIdsFlow: GetCartProductIdsFlowUseCase,
)
