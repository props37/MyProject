package ru.zarina.zarina.ui.screens.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.RecommendationType
import ru.zarina.zarina.usecase.old.catalog.GetRecommendationsUseCase
import ru.zarina.zarina.usecase.old.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.old.favorites.SetIsFavoriteUseCase
import ru.zarina.zarina.usecase.old.search.AddToSearchHistoryUseCase
import ru.zarina.zarina.usecase.old.search.GetAutocompleteUseCase
import ru.zarina.zarina.usecase.old.search.GetSearchHistoryUseCase
import ru.zarina.zarina.usecase.old.search.GetSearchPageUseCase
import ru.zarina.zarina.usecase.old.search.RemoveFromSearchHistoryUseCase
import ru.zarina.zarina.util.base.usecase.invoke

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
