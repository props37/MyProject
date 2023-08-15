package ru.zarina.zarina.ui.screens.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.RecommendationType
import ru.zarina.zarina.usecase.catalog.GetRecommendationsUseCase
import ru.zarina.zarina.usecase.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.search.AddToSearchHistoryUseCase
import ru.zarina.zarina.usecase.search.GetAutocompleteUseCase
import ru.zarina.zarina.usecase.search.GetSearchHistoryUseCase
import ru.zarina.zarina.usecase.search.GetSearchPageUseCase
import ru.zarina.zarina.usecase.search.RemoveFromSearchHistoryUseCase

@Factory
class SearchInteractor(
    private val getAutocompleteUseCase: GetAutocompleteUseCase,
    val getSearchPageUseCase: GetSearchPageUseCase,
    val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val addToSearchHistoryUseCase: AddToSearchHistoryUseCase,
    private val removeFromSearchHistoryUseCase: RemoveFromSearchHistoryUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
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

}
