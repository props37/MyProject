package ru.zarina.zarina.ui.screens.search

import org.koin.core.annotation.Factory
import ru.zarina.zarina.usecase.favorites.GetFavoriteIdsUseCase
import ru.zarina.zarina.usecase.search.GetAutocompleteUseCase
import ru.zarina.zarina.usecase.search.GetSearchHistoryUseCase
import ru.zarina.zarina.usecase.search.GetSearchPageUseCase
import ru.zarina.zarina.utils.clean.invoke

@Factory
class SearchInteractor(
    private val getAutocompleteUseCase: GetAutocompleteUseCase,
    val getSearchPageUseCase: GetSearchPageUseCase,
    val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
) {

    fun getSearchHistory() = getSearchHistoryUseCase()

    suspend fun getAutocomplete(query: String) =
        getAutocompleteUseCase(GetAutocompleteUseCase.Parameters(query))

}
