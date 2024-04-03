package ru.livetyping.zarina.ui.screens.search

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.domain.old.RecommendationType
import ru.livetyping.zarina.usecase.old.catalog.GetRecommendationsUseCase
import ru.livetyping.zarina.usecase.old.favorites.GetFavoriteIdsUseCase
import ru.livetyping.zarina.usecase.old.favorites.SetIsFavoriteUseCase
import ru.livetyping.zarina.usecase.old.search.AddToSearchHistoryUseCase
import ru.livetyping.zarina.usecase.old.search.GetAutocompleteUseCase
import ru.livetyping.zarina.usecase.old.search.GetSearchHistoryUseCase
import ru.livetyping.zarina.usecase.old.search.GetSearchPageUseCase
import ru.livetyping.zarina.usecase.old.search.RemoveFromSearchHistoryUseCase
import ru.livetyping.zarina.util.base.usecase.invoke

@Factory
class SearchInteractor(
    private val getAutocompleteUseCase: GetAutocompleteUseCase,
    val getSearchPageUseCase: GetSearchPageUseCase,
    val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addToSearchHistoryUseCase: AddToSearchHistoryUseCase,
    private val removeFromSearchHistoryUseCase: RemoveFromSearchHistoryUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val setIsFavoriteUseCase: SetIsFavoriteUseCase,
) {

    fun getSearchHistory(limit: Int) =
        getSearchHistoryUseCase(GetSearchHistoryUseCase.Params(limit))

    suspend fun getAutocomplete(query: String) =
        getAutocompleteUseCase(GetAutocompleteUseCase.Parameters(query))

    suspend fun addToSearchHistory(query: String) =
        addToSearchHistoryUseCase(AddToSearchHistoryUseCase.Params(query))

    suspend fun removeFromSearchHistory(query: String) =
        removeFromSearchHistoryUseCase(RemoveFromSearchHistoryUseCase.Params(query))

    fun getRecommendations() = getRecommendationsUseCase(
        GetRecommendationsUseCase.Params(RecommendationType.User)
    )

    suspend fun setIsFavorite(product: Product, isFavorite: Boolean) =
        setIsFavoriteUseCase(SetIsFavoriteUseCase.Params(product, isFavorite))

    fun getFavoriteIds() = getFavoriteIdsUseCase()

}
